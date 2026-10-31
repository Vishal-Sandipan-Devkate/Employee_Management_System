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

   The schema seeds an administrator account:

   - Username: `admin`
   - Password: `ChangeMe123!`

   Change this password in the database before deployment.

2. Configure the application without committing credentials:

   ```sh
   export DB_URL=''
   export DB_USER=''
   export DB_PASSWORD=''
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
- Session-based administrator authentication and CSRF protection for changes.
