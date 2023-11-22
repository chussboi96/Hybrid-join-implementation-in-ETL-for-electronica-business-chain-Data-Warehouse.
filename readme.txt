-- Data Warehousing Project --


Description
This project is an implementation of a data warehousing ETL process using Java 20 with MySQL as the database backend. The focus is on applying the HYBRIDJOIN algorithm for effective data transformation and loading, supported by JDBC connections for database interactions.

Prerequisites
Java JDK 20
IntelliJ IDEA (or similar Java IDE)
MySQL Community Edition (version 8)
MySQL Workbench

Connectors and Libraries
MySQL JDBC Driver (Connector/J 8.2.0)
Apache Commons Collections 4.4

Environment Setup
Download and install Java JDK 20 from the official site.
Install IntelliJ IDEA, which can be downloaded from JetBrains.
Install MySQL Community Edition and MySQL Workbench using the setups provided in the Google Drive link.
Setup MYSQL server 8.0


Project Setup
Clone the repository to your local machine by downloading and extracting.
Open IntelliJ IDEA and import the cloned project:
Choose Open or Import on the IntelliJ IDEA start page.
Navigate to the cloned project directory and select the project.
Follow the prompts in IntelliJ IDEA to complete the import.


In MySQL Workbench, create 2 DATABASES named transaction and master_data.
import the data from csv files into these databases.
Run the SQL script mysql-stuff/createDW.sql in MySQL Workbench to set up the data warehouse structure.


Running the Project
Add the MySQL JDBC Driver and Apache Commons Collections JARs to your project's classpath in IntelliJ IDEA:
Right-click on the project → Open Project Structure → Dependencies → Add → Select the JAR files from the downloaded location.
Open src/Main.java in IntelliJ IDEA.
Run src/Main.java by right-clicking on the file and selecting Run/Play button.


Analysis Queries
The analysis queries are located in queriesDW.sql.
Run these queries in MySQL Workbench to view the results of the data warehousing process.

Notes
This project is developed and tested on Windows 11 with Java 20. 
While it may run on Unix-based systems, this has not been validated.


GOOOD LUCKKK!! :))