Campus Maintenance Reporting System

TECHNOLOGY
- Java 17+ (built with JDK 21 on the developer machine, still Java 17
  language level, works fine on Java 17 or newer)
- Apache Maven
- Apache Tomcat 10.1 (jakarta.servlet namespace)
- Jakarta Servlets + JSP (MVC pattern), JSTL for JSP tags
- org.json library for building/reading JSON in the REST endpoint

APPLICATION URL / CONTEXT PATH
Once deployed to Tomcat, the app runs at:
    http://localhost:8080/campus-maintenance/

DEMONSTRATION ACCOUNTS
- Administrator (fixed account, already created when the app starts):
      username: admin
      password: Demo1234
- Student accounts: register your own through the "Register" link
  on the login page. Student number must be 6-12 digits, password
  must be at least 8 characters.

HOW TO BUILD AND RUN (macOS, NetBeans)
1. Install Java 17+ (or newer) and Apache Maven if not already installed.
   You can check with:  java -version   and   mvn -v
2. Install/point NetBeans to a local Apache Tomcat 10.1 server:
   Tools > Servers > Add Server > Apache Tomcat, browse to your
   Tomcat 10.1 install folder.
3. Open NetBeans > File > Open Project > select this project folder
   (the one containing pom.xml). NetBeans will recognise it as a
   Maven Web Application.
4. Right-click the project > Properties > Run, make sure the server
   is set to your Tomcat 10.1 instance and the context path is
   /campus-maintenance.
5. Right-click the project > Run (or Debug). NetBeans will run
   "mvn package", deploy the WAR file to Tomcat and open a browser.

HOW TO BUILD AND RUN (command line, without NetBeans)
1. From the project folder, run:
       mvn clean package
   This creates target/campus-maintenance.war
2. Copy that WAR file into <tomcat-install>/webapps/
3. Start Tomcat:
       <tomcat-install>/bin/startup.sh
4. Visit http://localhost:8080/campus-maintenance/

RUNNING THE JUNIT TESTS
   mvn test
Tests live in src/test/java and cover ValidationService and
ReportService (valid, invalid and boundary cases).

FILE UPLOAD STORAGE
Evidence files (JPG/PNG/PDF, max 2 MB) are saved with a random
generated file name in a folder outside the web app
(system temp folder + "/campus-maintenance-uploads"), so they cannot
be opened directly through a browser URL. They can only be downloaded
through the protected /evidence servlet, which checks that the
person asking for the file is either the report owner or the admin.

NOTES / KNOWN LIMITATIONS
- Data (users and reports) is kept in memory only, using the
  repository classes. Restarting Tomcat clears all data. A real
  system would use a database.
- Passwords are hashed with plain SHA-256 before being stored (see
  the comment in PasswordUtil.java) - a real system needs a salted,
  slow hashing algorithm such as bcrypt.
