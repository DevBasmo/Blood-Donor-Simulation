# Blood Bank Donor Management System Simulation

A JavaFX desktop application for managing blood donation 
appointments with role-based login for Admin and User access.

## Features
- Role-based login system (Admin and User)
- Account lockout after 3 failed login attempts
- Show and hide password toggle
- User can book blood donation appointments
- User can view their donation profile
- Admin can view all registered donors in a table
- Admin can search donors by blood group
- Full input validation on donation form
- Connected to a real database for persistent storage
- Fade transition animation on login screen

## Technologies Used
- Java
- JavaFX (BorderPane, GridPane, TableView, MenuBar)
- JavaFX Animation (FadeTransition)
- JavaFX UI Controls (ComboBox, DatePicker, PasswordField)
- MySQL Database
- JDBC (Java Database Connectivity)
- JavaFX CSS Styling
- Object-Oriented Programming (Donor, DatabaseConnection classes)
- NetBeans IDE

## Database Setup
This application uses a MySQL database.

- Create a database named `blood_donor_db`
- Create the required tables (or import the provided SQL file)
- Update your database credentials in `DatabaseConnection.java`:
  - URL: `jdbc:mysql://localhost:3306/blood_donor_db`
  - Username: your MySQL username
  - Password: your MySQL password

## How to Run
- Make sure you have Java JDK 8 or higher installed
- Make sure JavaFX is configured in your IDE
- Configure your database connection in DatabaseConnection.java
- Open the project in NetBeans
- Run the BloodDonorApplication.java file

Note: This project requires JavaFX libraries to be added 
to your project. JavaFX is not included by default in 
Java 11 and above.
