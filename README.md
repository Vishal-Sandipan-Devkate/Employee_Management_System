Here is the consolidated list of all valid terminal and MySQL commands executed across both logs, categorized by component, along with their brief usage.

---

### 1. System & Service Management (`systemctl` / `pkill`)

| Command | Usage |
| --- | --- |
| `sudo pkill mysqld` | Forcefully terminates all running MySQL server daemon processes. |
| `sudo systemctl start mysql.service` | Starts the MySQL database service via `systemd`. |
| `sudo systemctl status mysql.service` | Displays the current running state, PID, memory, and runtime log snippet of the MySQL service. |

---

### 2. MySQL Shell Commands & Statements

#### **CLI Invocation**

| Command | Usage |
| --- | --- |
| `mysql -u root` | Logs into MySQL as `root` without prompting for a password (uses socket/auth plugin if configured). |
| `mysql -u root -p` | Connects to MySQL as `root` and prompts interactively for password authentication. |
| `mysql --version` | Prints the installed MySQL server and client version information. |
| `mysql --help` | Displays all available command-line flags and configuration options for the MySQL client. |

#### **SQL Statements (Inside MySQL Shell)**

| Statement | Usage |
| --- | --- |
| `FLUSH PRIVILEGES;` | Reloads grant tables from MySQL system databases so privilege changes take effect immediately. |
| `ALTER USER 'root'@'localhost' IDENTIFIED BY 'Root@123';` | Changes the password for the local `root` user to `Root@123`. |
| `SELECT user, host, plugin FROM mysql.user;` | Displays all database users, their allowed host connections, and their authentication methods. |
| `CREATE DATABASE sat;` | Creates a new database named `sat`. |
| `USE sat;` | Switches the active database session context to `sat`. |
| `CREATE TABLE emp (...);` | Defines a new table named `emp` with specified columns (`name`, `email`, `department`). |
| `DESC emp;` | Displays column names, data types, null constraints, and defaults for table `emp`. |
| `SELECT * FROM emp;` | Fetches and displays all columns and rows stored inside table `emp`. |
| `EXIT;` / `quit` | Closes the active MySQL interactive shell session. |

---

### 3. File System & Directory Navigation

| Command | Usage |
| --- | --- |
| `ls` | Lists files and directories in the current working directory. |
| `ls /opt` | Lists contents of the system directory `/opt`. |
| `cd ~` | Navigates directly to the current user's home directory (`/home/vishaldevkate`). |
| `cd ..` | Moves up one level to the parent directory. |
| `mkdir -p WEB-INF/classes` | Creates nested directories (`WEB-INF/classes`), creating parent folders if they don't exist. |
| `find . -type f` | Recursively finds and lists all regular files starting from the current directory. |
| `find WEB-INF/classes -type f` | Lists all files inside `WEB-INF/classes` to verify compiled `.class` files. |

---

### 4. File Download & Utility Tooling

| Command | Usage |
| --- | --- |
| `wget <URL>` | Downloads files directly from the web via HTTP/HTTPS (used to fetch `mysql-connector-j-9.0.0.jar`). |
| `chmod +x *.sh` | Grants executable (`+x`) permissions to all shell script files in the directory. |
| `vim <filename>` | Opens or creates a file inside the terminal-based Vim text editor. |
| `code .` | Opens the current directory as a project workspace in Visual Studio Code. |
| `firefox <URL>` | Launches the Firefox web browser and opens the specified HTTP URL. |

---

### 5. Apache Tomcat Control Scripts

| Command | Usage |
| --- | --- |
| `./startup.sh` | Launches the Apache Tomcat server instance in the background. |
| `./shutdown.sh` | Gracefully stops the running Apache Tomcat server instance. |

---

### 6. Java Compilation (`javac`)

| Command | Usage |
| --- | --- |
| `javac -cp ~/apache-tomcat-10.1.60/lib/servlet-api.jar -d WEB-INF/classes src/mcoe/Register.java` | Compiles `Register.java` using Tomcat's `servlet-api.jar` in the classpath and places compiled `.class` output into `WEB-INF/classes/`. |

---

### 7. Git Version Control

| Command | Usage |
| --- | --- |
| `git init` | Initializes a new local Git repository in the current working directory. |
| `git status` | Displays the state of working directory files (untracked, modified, or staged). |
| `git add .` | Stages all new, modified, and deleted files in the project for the next commit. |
| `git commit -m 'message'` | Records staged changes into repository history with a descriptive commit message. |
