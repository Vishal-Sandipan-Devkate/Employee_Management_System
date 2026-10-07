package mcoe;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet("/Register")
public class Register extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String department = request.getParameter("department");

        String jdbcURL =
            "jdbc:mysql://localhost:3306/sat";

        String dbUser = "root";

        String dbPassword = "Root@123";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection =
                DriverManager.getConnection(
                    jdbcURL,
                    dbUser,
                    dbPassword
                );

            String sql =
                "INSERT INTO emp (name, email, department) " +
                "VALUES (?, ?, ?)";

            PreparedStatement statement =
                connection.prepareStatement(sql);

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, department);

            int rowsInserted =
                statement.executeUpdate();

            if (rowsInserted > 0) {

                out.println(
                    "<h3>Employee registered successfully!</h3>"
                );

            } else {

                out.println(
                    "<h3>Error registering employee.</h3>"
                );
            }

            statement.close();
            connection.close();

        } catch (Exception e) {

            out.println(
                "<h3>Error: " + e.getMessage() + "</h3>"
            );

            e.printStackTrace(out);
        }
    }
}
