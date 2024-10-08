# bankManagement

## Overview

The Bank Management Application is a backend system designed to manage essential banking operations. It handles core banking functionalities, including account management, transaction tracking, and beneficiary services. The system is built with Java 17, Spring Boot 3, and JPA, offering RESTful APIs for integration with other services or front-end applications.

This application includes:

Management of Beneficiaries (Bank account holders).
Tracking of Accounts associated with beneficiaries.
Processing Transactions (withdrawals and deposits) for accounts.
Querying detailed information about accounts and transaction histories.

## Running the Application

Prerequisites
Java 17+

You can run the application using the provided run-bank-management.bat file:
Navigate to the project directory.
Ensure that you set the JAVA_HOME environment variable to your local path to java 17
Run the following command:
run-bank-management.bat

This will start the Spring Boot application and import the CSV data into the in-memory H2 database.

Once the application is running, you can access the H2 database at:
http://localhost:8080/h2-console

The JDBC URL is (no password needed):
    url: jdbc:h2:mem:mydb
    username: sa



## RESTful API Endpoints
The application provides various RESTful APIs for interacting with the system.

API Endpoints:
- Get Beneficiary by ID
  GET /api/beneficiaries/{id}
  Returns the details of a specific beneficiary.

- Get Accounts of a Beneficiary
GET /api/beneficiaries/{id}/accounts
Returns the list of accounts for a specific beneficiary.

- Get Transactions of a Beneficiary
GET /api/beneficiaries/{id}/transactions
Returns all transactions for a specific beneficiary's accounts.

- Get Largest Withdrawal for Last Month
GET /api/beneficiaries/{id}/largest-withdrawal-last-month
Returns the largest withdrawal made by a beneficiary in the previous month.

You can explore and try out the REST APIs using Swagger. Access the Swagger UI at the following URL:

- Swagger UI: http://localhost:8080/swagger-ui.html
This interface provides a comprehensive overview of the available endpoints, along with their descriptions, request parameters, and response formats.

## Log Files
The application logs can be found in the logs folder. These logs capture important runtime information, which can be useful for debugging and monitoring the application's behavior.

## Technologies Used
- Java 17
- Spring Boot 3
- Maven
- H2 Database (for development and testing): An in-memory database to quickly start and test the application. Can be configured for production databases like MySQL or PostgreSQL.
