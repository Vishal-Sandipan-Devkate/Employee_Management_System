# Employee Management System

This project is a Java Servlet employee CRUD application backed by MySQL. It
requires Java 21, Maven, Tomcat 10.1+, and MySQL 8+.

## Setup

1. Create the database and application user:

   ```sh
   mysql -u root -p < schema.sql
   ```

   Change the placeholder password in `schema.sql` before using it outside a
   local development environment.

2. Configure the application without committing credentials:

   ```sh
   export DB_URL='jdbc:mysql://localhost:3306/sat'
   export DB_USER='employee_app'
   export DB_PASSWORD='your-application-password'
   ```

3. Build the deployable WAR:

   ```sh
   mvn clean verify
   ```

4. Deploy `target/employee-registration.war` to Tomcat 10.1+ and open
   `/employee-registration/home.html`.

## Features

- Create, list, edit, and deactivate employees.
- Server-side name, email, and department validation.
- Unique email enforcement.
- Prepared statements and try-with-resources for database access.
- Technical errors are logged server-side instead of exposed to users.

The application currently has no authentication. Add administrator
authentication and authorization before deploying it to a shared or public
environment.
