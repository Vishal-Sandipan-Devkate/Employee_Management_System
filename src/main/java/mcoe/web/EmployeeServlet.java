package mcoe.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mcoe.dao.EmployeeDao;
import mcoe.model.Employee;
import mcoe.security.Csrf;
import mcoe.validation.EmployeeValidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {
    private final EmployeeDao employeeDao = new EmployeeDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String editId = request.getParameter("edit");
        if (editId == null || editId.isBlank()) {
            if ("1".equals(request.getParameter("new"))) {
                renderForm(request, response, new Employee(0, "", "", "", "ACTIVE", null, null), null);
            } else {
                renderList(request, response, null);
            }
            return;
        }
        try {
            long id = Long.parseLong(editId);
            Optional<Employee> employee = employeeDao.findById(id);
            if (employee.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found");
                return;
            }
            renderForm(request, response, employee.get(), null);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid employee ID");
        } catch (SQLException e) {
            log("Unable to load employee", e);
            renderError(response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        if (!Csrf.isValid(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }
        String action = request.getParameter("action");
        try {
            if ("deactivate".equals(action)) {
                employeeDao.deactivate(parseId(request.getParameter("id")));
                response.sendRedirect(request.getContextPath() + "/employees");
                return;
            }

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String department = request.getParameter("department");
            List<String> errors = EmployeeValidator.validate(name, email, department);
            if (!errors.isEmpty()) {
                renderForm(request, response, formEmployee(request), errors);
                return;
            }

            String normalizedName = name.trim();
            String normalizedEmail = email.trim().toLowerCase();
            String normalizedDepartment = department.trim();
            String id = request.getParameter("id");
            if (id == null || id.isBlank()) {
                employeeDao.create(normalizedName, normalizedEmail, normalizedDepartment);
            } else {
                employeeDao.update(parseId(id), normalizedName, normalizedEmail, normalizedDepartment);
            }
            response.sendRedirect(request.getContextPath() + "/employees");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid employee ID");
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                renderForm(request, response, formEmployee(request), List.of("That email address is already registered."));
            } else {
                log("Unable to save employee", e);
                renderError(response);
            }
        }
    }

    private long parseId(String value) {
        return Long.parseLong(value);
    }

    private Employee formEmployee(HttpServletRequest request) {
        String id = request.getParameter("id");
        return new Employee(
                id == null || id.isBlank() ? 0 : Long.parseLong(id),
                request.getParameter("name"),
                request.getParameter("email"),
                request.getParameter("department"),
                "ACTIVE", null, null);
    }

    private void renderList(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        try {
            List<Employee> employees = employeeDao.findActive();
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            pageStart(out, "Employees");
            out.println("<h1>Employees</h1><p><a class='button' href='employees?new=1'>Add employee</a></p>");
            out.printf("<form class='logout' method='post' action='logout'><input type='hidden' name='csrfToken' value='%s'>"
                    + "<button type='submit'>Log out</button></form>", escape(Csrf.token(request.getSession())));
            if (message != null) out.println("<p class='success'>" + escape(message) + "</p>");
            out.println("<table><tr><th>Name</th><th>Email</th><th>Department</th><th>Actions</th></tr>");
            for (Employee employee : employees) {
                out.printf("<tr><td>%s</td><td>%s</td><td>%s</td><td>"
                                + "<a href='employees?edit=%d'>Edit</a> "
                                + "<form class='inline' method='post' action='employees'>"
                                + "<input type='hidden' name='action' value='deactivate'>"
                                + "<input type='hidden' name='csrfToken' value='%s'>"
                                + "<input type='hidden' name='id' value='%d'>"
                                + "<button type='submit'>Deactivate</button></form></td></tr>%n",
                        escape(employee.name()), escape(employee.email()), escape(employee.department()),
                        escape(Csrf.token(request.getSession())),
                        employee.id(), employee.id());
            }
            out.println("</table>");
            pageEnd(out);
        } catch (SQLException e) {
            log("Unable to list employees", e);
            renderError(response);
        }
    }

    private void renderForm(HttpServletRequest request, HttpServletResponse response,
                            Employee employee, List<String> errors)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        pageStart(out, employee.id() == 0 ? "Add Employee" : "Edit Employee");
        out.printf("<h1>%s</h1>", employee.id() == 0 ? "Add Employee" : "Edit Employee");
        if (errors != null) {
            out.println("<div class='errors'><ul>");
            errors.forEach(error -> out.println("<li>" + escape(error) + "</li>"));
            out.println("</ul></div>");
        }
        out.printf("<form method='post' action='employees'>%s"
                        + "<input type='hidden' name='csrfToken' value='%s'>"
                        + "<label>Name <input name='name' maxlength='100' required value='%s'></label>"
                        + "<label>Email <input type='email' name='email' maxlength='255' required value='%s'></label>"
                        + "<label>Department <input name='department' maxlength='100' required value='%s'></label>"
                        + "<button type='submit'>Save</button> <a href='employees'>Cancel</a></form>",
                employee.id() == 0 ? "" : "<input type='hidden' name='id' value='" + employee.id() + "'>",
                escape(Csrf.token(request.getSession())),
                escape(employee.name()), escape(employee.email()), escape(employee.department()));
        pageEnd(out);
    }

    private void renderError(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<h1>Something went wrong</h1><p>Please try again later.</p>");
    }

    private void pageStart(PrintWriter out, String title) {
        out.printf("<!doctype html><html><head><meta charset='UTF-8'><title>%s</title>"
                + "<link rel='stylesheet' href='css/style.css'></head><body><main>", escape(title));
    }

    private void pageEnd(PrintWriter out) {
        out.println("</main></body></html>");
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }
}
