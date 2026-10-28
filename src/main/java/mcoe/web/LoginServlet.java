package mcoe.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mcoe.dao.UserDao;
import mcoe.security.Csrf;
import mcoe.security.PasswordHasher;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/employees");
            return;
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!doctype html><html><head><meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1'>"
                + "<title>Admin Login</title><link rel='stylesheet' href='css/style.css'></head><body><main>");
        out.println("<h1>Admin Login</h1>");
        if ("1".equals(request.getParameter("error"))) {
            out.println("<div class='errors'><p>Invalid username or password.</p></div>");
        }
        out.println("<form method='post' action='login'>"
                + "<label>Username <input name='username' required autocomplete='username'></label>"
                + "<label>Password <input type='password' name='password' required autocomplete='current-password'></label>"
                + "<button type='submit'>Log in</button></form></main></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            var user = username == null || password == null
                    ? java.util.Optional.<UserDao.User>empty()
                    : userDao.findEnabledByUsername(username.trim());
            if (user.isEmpty() || !PasswordHasher.matches(password.toCharArray(), user.get().passwordHash())) {
                response.sendRedirect(request.getContextPath() + "/login?error=1");
                return;
            }
            var session = request.getSession(true);
            session.invalidate();
            session = request.getSession(true);
            session.setAttribute("authenticatedUser", user.get().username());
            session.setAttribute("role", user.get().role());
            Csrf.token(session);
            response.sendRedirect(request.getContextPath() + "/employees");
        } catch (SQLException e) {
            log("Unable to authenticate user", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        return request.getSession(false) != null
                && request.getSession(false).getAttribute("authenticatedUser") != null;
    }
}
