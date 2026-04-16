import java.util.List;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BloodDonorApplication extends Application {
    
    private double STAGE_WIDTH = 900;
    private double STAGE_HEIGHT = 630;
    private ComboBox<String> roleBox;
    private Label heading = new Label("Blood Bank !");
    private DatabaseConnection database;
    private int failedAttempts = 0;
    private Donor currentDonor;
    
    public static void main( String [] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage)  {
        database = new DatabaseConnection();
        stage.setTitle("Blood Donor App!");
        Scene scene = new Scene(LoginTab(stage), STAGE_WIDTH, STAGE_HEIGHT);
        stage.setScene(scene);
        stage.show();
        
        
        
        
    }
    
    
    //Loggin Tab
    private BorderPane LoginTab(Stage stage)
    {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-padding: 40; -fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 15;");
        
        ImageView logo = new ImageView( new Image("blodsD.jpg"));
        logo.setFitHeight(75);
        logo.setFitWidth(160);
        logo.setPreserveRatio(true);
        
        heading.setStyle("-fx-font-size: 45px;"+
                "-fx-font-weight: bold;"+
                "-fx-text-fill: #1e1e1e;"+
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 2, 0.5, 1, 1);"+
                "-fx-alignment: center;"        
        );
        
        VBox topBanner = new VBox();
        topBanner.setAlignment(Pos.CENTER);
        topBanner.getChildren().addAll(logo, heading);
        topBanner.setPadding(new Insets(20, 0, 20, 0));
        
        //username
        Label usernameLabel  = new Label ("Username:");
        usernameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366");
        
        TextField usernameField = new TextField();
        usernameField.setFont(Font.font("Inter", 14));
        usernameField.setPromptText("Enter your user name ");
        usernameField.setMaxWidth(550);
        
        //password
        double passwordFieldHeight  = 36;
        Label passLabel =  new Label("Password:");
        passLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366");
        PasswordField passField = new PasswordField();
        passField.setFont(Font.font("Inter", 14));
        passField.setPromptText("Enter your password");
        passField.setPrefHeight(passwordFieldHeight);
        passField.setMaxHeight(passwordFieldHeight);
        passField.setMinHeight(passwordFieldHeight);
 
        passField.setMaxWidth(550);
        
        
        
        
        TextField visiblePassField = new TextField();
        visiblePassField.setFont(Font.font("Inter, 14"));
        visiblePassField.setMaxWidth(550);
        visiblePassField.setManaged(false);
        visiblePassField.setVisible(false);
        visiblePassField.setPrefHeight(passwordFieldHeight);
        visiblePassField.setMaxHeight(passwordFieldHeight);
        visiblePassField.setMinHeight(passwordFieldHeight);
        
        visiblePassField.textProperty().bindBidirectional(passField.textProperty());
      
        
        
        CheckBox showPassword = new CheckBox("Show Password");
        showPassword.setStyle("-fx-font-size: 12px;");
        
        showPassword.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            visiblePassField.setVisible(isSelected);
            visiblePassField.setManaged(isSelected);
            passField.setVisible(!isSelected);
            passField.setManaged(!isSelected);
        
    
                });
        StackPane passwordBox = new StackPane(passField,visiblePassField);
        
        
        
        //Role
        
        
        //role
        Label rolee = new Label("Role:");
        rolee.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366");
        
        roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Admin", "User");
        roleBox.setPromptText("Select Role");
        roleBox.setMaxWidth(550);
     
        roleBox.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 14px;");
        
        
        
        //Login Button
      
        
        Button loginButton = new Button("Login");
        passField.setOnAction(e -> loginButton.fire());
        loginButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #003366, #0055a5);"+
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;"  +
                "-fx-font-weight:bold;" +
                "-fx-padding:8 20 8 20;"+
                "-fx-background-radius: 10;"+
                "-fx-cursor: hand;"        
                
        );
     
    
       
        
        loginButton.setOnAction(e -> 
        {
            
            String username = usernameField.getText();
            String password =  passField.getText();
            String role = (String) roleBox.getValue();
            
            if ( username.isEmpty() || password.isEmpty() || role == null)
            {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields!");
                return;
            }
            boolean success = database.validateLogin(username, password, role);
            
            if ( success )
            {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome" + username+"(" + role +")");
                failedAttempts = 0;
                
                if (role.equalsIgnoreCase("Admin"))
                {
                    
                    stage.setScene(new Scene(adminTab(stage),STAGE_WIDTH, STAGE_HEIGHT));
                }
                
                else if ( role.equalsIgnoreCase("User"))
                {
                    stage.setScene(new Scene(userTab(stage),STAGE_WIDTH, STAGE_HEIGHT));
                }
             //   else
              //  {
              //      showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username, password, or role!");
              //  }
            }
            else
            {
                failedAttempts++;
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username, password, or role!");
                
                if ( failedAttempts >= 3)
                {
                    showAlert(Alert.AlertType.ERROR, "Too Many Attempts ", "You have entered wrong details 3 times. The app will now close.");
                    stage.close();
                }
                
            }
        });
       HBox button = new HBox (loginButton);
       button.setAlignment(Pos.CENTER);
     
        
        VBox loginField = new VBox(10);
        loginField.setAlignment(Pos.CENTER_LEFT);
        loginField.setPrefWidth(550);
        loginField.setMaxWidth(550);
        
        loginField.getChildren().addAll(usernameLabel, usernameField, passLabel, passwordBox, showPassword, rolee, roleBox, button );
        
       StackPane centerBox = new StackPane(loginField);
       centerBox.setAlignment(Pos.CENTER);
       
       layout.setTop(topBanner);
       layout.setCenter(centerBox); 
       layout.setTop(topBanner);
       FadeTransition ft = new FadeTransition(Duration.millis(700), layout);
       return layout;
       // return null;
        
        
        
    }
    
    public BorderPane adminTab(Stage stage)
    {
       BorderPane adminPage = new BorderPane();
       //Menu Bar
       MenuBar menuBar = new MenuBar();
       
       
       //Menu
       Menu homeMenu = new Menu("home");
       Menu reportMenu = new Menu("Report");
       Menu accountMenu = new Menu("Acount");
       
     
       //Homemenu item
       MenuItem goHome  = new MenuItem("Go to Home");
       goHome.setOnAction(e -> 
       {
           adminPage.setCenter(adminHomeView());
               });
       homeMenu.getItems().add(goHome);
       
       
       
       //Report Item
       //view donors
       MenuItem viewDonors = new MenuItem("View Donors");
       viewDonors.setOnAction(e -> 
       {
           
           adminPage.setCenter(viewDonorsPage());
           
       }
       );
       
       
       //searchDonors
       MenuItem searchDonors= new MenuItem("Search Donor");
       searchDonors.setOnAction(e ->
       {
           adminPage.setCenter(searchDonorPage());
           
           
       });
       reportMenu.getItems().addAll(viewDonors,searchDonors);
       
      
       //Account MenuItem
       MenuItem logout = new MenuItem("Logout");
       logout.setOnAction(e -> {
           stage.setScene(new Scene(LoginTab(stage),STAGE_WIDTH, STAGE_HEIGHT));
       });
       MenuItem closeApp = new MenuItem("Close App");
       closeApp.setOnAction(e -> 
       {
           stage.close();
       }
       );
       accountMenu.getItems().addAll(logout,closeApp);
       
       
       menuBar.getMenus().addAll(homeMenu,reportMenu,accountMenu);
       
       
       adminPage.setTop(menuBar);
       adminPage.setCenter(adminHomeView());
       
       return adminPage;
      
    }
   
    
    
    
    public BorderPane userTab( Stage stage)
    {
        BorderPane userPage = new BorderPane();
        
       //Menu Bar
       MenuBar menuBar = new MenuBar();
       //Menus
       Menu homeMenu = new Menu("Home");
       Menu profileMenu = new Menu("Profile");
       Menu donateMenu  = new Menu("Donate");
       Menu accountMenu = new Menu("Acount");
       
       
       //Homemenu item
       MenuItem goHome  = new MenuItem("Go to Home");
       goHome.setOnAction(e -> 
       {
           userPage.setCenter(userHomeView());
               });
       homeMenu.getItems().add(goHome);
      
       
       
       //Profile MenuItem
       MenuItem viewProfile = new MenuItem("View Profile");
       viewProfile.setOnAction(e -> 
       {
           userPage.setCenter(viewProfile(stage));
       }
       );
       profileMenu.getItems().add(viewProfile);
       
       
       //Donate MenuItem
       MenuItem donateBlood = new MenuItem("Donate Blood");
       donateBlood.setOnAction(e -> 
       {
           userPage.setCenter(donateBloodView(stage));
           
       }
       );
       donateMenu.getItems().add(donateBlood);
       
       //accountMenu item
       MenuItem logout = new MenuItem("Logout");
       logout.setOnAction(e -> {
           stage.setScene(new Scene(LoginTab(stage),STAGE_WIDTH, STAGE_HEIGHT));
       });
       MenuItem closeApp = new MenuItem("Close App");
       closeApp.setOnAction(e -> 
       {
           stage.close();
       }
       );
       accountMenu.getItems().addAll(logout,closeApp);
       
       menuBar.getMenus().addAll(homeMenu,profileMenu,donateMenu,accountMenu);
       
 
       
       userPage.setTop(menuBar);
       userPage.setCenter(userHomeView());
       
       return userPage;
      
        
    }
    
    private void showAlert(Alert.AlertType type, String title, String message)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private StackPane userHomeView()
    {
         Label welcomeLabel = new Label("Welcome User! You are logged in.");
       welcomeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #003366;");
       return new StackPane(welcomeLabel);
        
    }
    
    private StackPane adminHomeView()
    {
          Label welcomeLabel = new Label("Welcome Admin! You are logged in.");
       welcomeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #003366;");
       return new StackPane(welcomeLabel);
      
    }
    private  BorderPane donateBloodView(Stage stage)
    {
        BorderPane donatePane = new BorderPane();
        
        //Tittle Top
        Label title = new Label("Blood Donation Appointment");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #003366;");
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(20, 0, 10, 0));
        donatePane.setTop(title);
        
        GridPane form = new GridPane();
        form.setHgap(140);
        form.setVgap(10);
        form.setPadding(new Insets(15));
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.NEVER);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        form.getColumnConstraints().addAll(col1, col2);
        
        //Name Field
        Label nameLabel = new Label("Full Name:");
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextField nameField = new TextField();
        nameField.setFont(Font.font("Inter", 14));
        nameField.setPromptText("Enter full name");
        nameField.setMaxWidth(Double.MAX_VALUE);
        nameField.setPrefWidth(20);
        
        //Blood Field
        Label bloodGroupLabel = new Label("Blood Group:");
        bloodGroupLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        ComboBox<String> bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-",  "AB+", "AB-", "O+", "O-");
        bloodGroupCombo.setPromptText("Select Blood Group");
        bloodGroupCombo.setMaxWidth(Double.MAX_VALUE);
        
        //Age Field
        Label ageLabel = new Label("Age:");
        ageLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextField ageField  = new TextField();
        ageField.setFont(Font.font("Inter", 14));
        ageField.setPromptText("Enter your age");
        
        //Gender Field
        Label genderLabel = new Label("Gender");
        genderLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male","Female","Other");
        genderCombo.setPromptText("Select Gender");
        genderCombo.setMaxWidth(Double.MAX_VALUE);
        
        
        //Select Date
        Label dateLabel = new Label("Appointment Date:");
        dateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        DatePicker datePicker  = new DatePicker();
        datePicker.setPromptText("Select Date");
        datePicker.setMaxWidth(Double.MAX_VALUE);
        
        
        
        //Contact Label
        Label contactLabel = new Label("Contact Label:");
        contactLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextField contactField = new TextField();
        contactField.setAlignment(Pos.BASELINE_RIGHT);
        contactField.setFont(Font.font("Inter", 14));
        contactField.setPromptText("Enter phone number");
        
        
        //Email
        Label emailLabel = new Label("Email Address:");
        emailLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextField emailField = new TextField();
        emailField.setFont(Font.font("Inter", 14));
        emailField.setPromptText("Enter Email Address");
        
        
        //Weight
        Label weightLabel = new Label("Weight (kg):");
        weightLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        ComboBox<String> weightCombo = new ComboBox<>();
        weightCombo.setEditable(true);
        weightCombo.getItems().addAll("50", "55", "60", "65", "70", "75", "80", "85", "90","95", "100");
        weightCombo.setPromptText("Select Your Weight");
        weightCombo.setMaxWidth(Double.MAX_VALUE);
        
        //Health Condition
        Label healthLabel = new Label("Health Condition");
        healthLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        ComboBox<String> healthCombo = new ComboBox<>();
        healthCombo.getItems().addAll("Normal", "Under Medication");
        healthCombo.setPromptText("Select Health Condition");
        healthCombo.setMaxWidth(Double.MAX_VALUE);
        
        //Medication
        Label medicationLabel = new Label("Current Medications:");
        medicationLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextField medicationField = new TextField();
        medicationField.setFont(Font.font("Inter", 14));
        medicationField.setPromptText("List any medications(if any)");
        
        
        //Note
        Label noteLabel = new Label("Note:");
        noteLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextArea noteField = new TextArea();
        noteField.setPromptText("Add any information or suggestion..");
        noteField.setPrefRowCount(4);
        noteField.setWrapText(true);
        
        
        
        
        
        
        //Adding on grid
        form.add(nameLabel, 0, 0);
        form.add(nameField,1, 0);
        form.add(bloodGroupLabel, 0, 1);
        form.add(bloodGroupCombo, 1, 1);
        form.add(ageLabel, 0, 2);
        form.add(ageField, 1, 2);
        form.add(genderLabel, 0, 3);
        form.add(genderCombo, 1, 3);
        form.add(dateLabel, 0, 4);
        form.add(datePicker, 1, 4);
        form.add(contactLabel, 0, 5);
        form.add(contactField, 1, 5);
        form.add(emailLabel, 0, 6);
        form.add(emailField, 1, 6);
        form.add(weightLabel, 0, 7);
        form.add(weightCombo, 1, 7);
        form.add(healthLabel, 0, 8);
        form.add(healthCombo, 1, 8);
        form.add(medicationLabel, 0, 9);
        form.add(medicationField,1, 9);
        form.add(noteLabel, 0, 10);
        form.add(noteField, 1, 10);
        Platform.runLater(() ->form.requestFocus());
        
        //Submit button
        Button submitButton = new Button("Book Now");
        submitButton.setStyle("-fx-background-color: #003366; -fx-text-fill:white; -fx-font-size: 16px;");
        submitButton.setPrefWidth(100);
        HBox buttonBox = new HBox(submitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding( new Insets(20, 0, 0, 0));
        
       submitButton.setOnAction(e -> {
    DatabaseConnection database = new DatabaseConnection();

    try {
        
        if (nameField.getText().trim().isEmpty() ||
                bloodGroupCombo.getValue() == null ||
                ageField.getText().trim().isEmpty() ||
                genderCombo.getValue() == null ||
                datePicker.getValue() == null ||
                contactField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                weightCombo.getValue() == null ||
                healthCombo.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please fill in all required fields.");
            return;
        }

        
        int age;
        try {
            age = Integer.parseInt(ageField.getText().trim());
            if (age < 18) {
                showAlert(Alert.AlertType.WARNING, "Invalid Age", "You must be at least 18 years old to donate.");
                return;
            }
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.WARNING, "Invalid Age", "Please enter a valid number for age.");
            return;
        }

       
        String name = nameField.getText().trim();
        String bloodGroup = bloodGroupCombo.getValue();
        String gender = genderCombo.getValue();
        java.sql.Date appointmentDate = java.sql.Date.valueOf(datePicker.getValue());
        String contact = contactField.getText().trim();
        String email = emailField.getText().trim();
        String weight = weightCombo.getValue();
        String healthCondition = healthCombo.getValue();
        String medication = medicationField.getText().trim();
        String note = noteField.getText().trim();

     
        boolean success = database.addDonor(name, bloodGroup, age, gender, appointmentDate,
                contact, email, weight, healthCondition, medication, note);

        
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment booked successfully!");

            
            currentDonor = new Donor(name, bloodGroup, age, gender, contact, email,
                    weight, healthCondition, medication, note, appointmentDate);

            // Clear form
            nameField.clear();
            bloodGroupCombo.setValue(null);
            ageField.clear();
            genderCombo.setValue(null);
            datePicker.setValue(null);
            contactField.clear();
            emailField.clear();
            weightCombo.setValue(null);
            healthCombo.setValue(null);
            medicationField.clear();
            noteField.clear();

             } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save appointment. Please try again.");
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + ex.getMessage());
    }

     finally
            {
                database.closeConnection();
            }
            
            
        }
        );
        
         
        
        
        //CenterBox
        VBox centerBox = new VBox(5,form, buttonBox);
        centerBox.setPadding(new Insets(20));
        donatePane.setCenter(centerBox);
        
        donatePane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        form.setMaxWidth(Double.MAX_VALUE);
        form.setMaxHeight(Double.MAX_VALUE);
        BorderPane.setAlignment(centerBox, Pos.CENTER);
        
        
        return donatePane;
        
        
        
        
    }
    private BorderPane viewProfile( Stage stage)
    {
        BorderPane display = new BorderPane();
        
          //Tittle Top
        Label title = new Label("Profile View");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #003366;");
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(20, 0, 10, 0));
        display.setTop(title);
        
        GridPane form = new GridPane();
        form.setHgap(140);
        form.setVgap(10);
        form.setPadding(new Insets(15));
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.NEVER);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        form.getColumnConstraints().addAll(col1, col2);
        
        //Name Field
        Label nameLabel = new Label("Full Name:");
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextField nameField = new TextField();
        nameField.setFont(Font.font("Inter", 14));
        nameField.setEditable(false);
        nameField.setMaxWidth(Double.MAX_VALUE);
        nameField.setPrefWidth(20);
        
        //Blood Field
        Label bloodGroupLabel = new Label("Blood Group:");
        bloodGroupLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextField bloodGroupField = new TextField();
        bloodGroupField.setEditable(false);
        bloodGroupField.setMaxWidth(Double.MAX_VALUE);
        bloodGroupField.setPrefWidth(20);
        
        //Age Field
        Label ageLabel = new Label("Age:");
        ageLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextField ageField  = new TextField();
        ageField.setFont(Font.font("Inter", 14));
        ageField.setEditable(false);
        
        //Gender Field
        Label genderLabel = new Label("Gender");
        genderLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextField genderField = new TextField();
        genderField.setFont(Font.font("Inter", 14));
        genderField.setMaxWidth(Double.MAX_VALUE);
        genderField.setEditable(false);
        
        
        //Select Date
        Label dateLabel = new Label("Appointment Date:");
        dateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        DatePicker datePicker  = new DatePicker();
        datePicker.setEditable(false);
        datePicker.setDisable(true);
        datePicker.setMaxWidth(Double.MAX_VALUE);
        
        
        
        //Contact Label
        Label contactLabel = new Label("Contact Label:");
        contactLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextField contactField = new TextField();
        contactField.setAlignment(Pos.BASELINE_RIGHT);
        contactField.setFont(Font.font("Inter", 14));
        contactField.setEditable(false);
        
        
        //Email
        Label emailLabel = new Label("Email Address:");
        emailLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextField emailField = new TextField();
        emailField.setFont(Font.font("Inter", 14));
        emailField.setEditable(false);
        
        
        //Weight
        Label weightLabel = new Label("Weight (kg):");
        weightLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextField weightField = new TextField();
        weightField.setMaxWidth(Double.MAX_VALUE);
        weightField.setEditable(false);
        
        //Health Condition
        Label healthLabel = new Label("Health Condition");
        healthLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextField healthField = new TextField();
        healthField.setMaxWidth(Double.MAX_VALUE);
        healthField.setEditable(false);
        
        //Medication
        Label medicationLabel = new Label("Current Medications:");
        medicationLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextField medicationField = new TextField();
        medicationField.setFont(Font.font("Inter", 14));
        medicationField.setEditable(false);
        
        
        //Note
        Label noteLabel = new Label("Note:");
        noteLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #003366;");
        TextArea noteField = new TextArea();
        noteField.setEditable(false);
        noteField.setPrefRowCount(4);
        noteField.setWrapText(true);
        
        
        
        
        
        
        //Adding on grid
        form.add(nameLabel, 0, 0);
        form.add(nameField,1, 0);
        form.add(bloodGroupLabel, 0, 1);
        form.add(bloodGroupField, 1, 1);
        form.add(ageLabel, 0, 2);
        form.add(ageField, 1, 2);
        form.add(genderLabel, 0, 3);
        form.add(genderField, 1, 3);
        form.add(dateLabel, 0, 4);
        form.add(datePicker, 1, 4);
        form.add(contactLabel, 0, 5);
        form.add(contactField, 1, 5);
        form.add(emailLabel, 0, 6);
        form.add(emailField, 1, 6);
        form.add(weightLabel, 0, 7);
        form.add(weightField, 1, 7);
        form.add(healthLabel, 0, 8);
        form.add(healthField, 1, 8);
        form.add(medicationLabel, 0, 9);
        form.add(medicationField,1, 9);
        form.add(noteLabel, 0, 10);
        form.add(noteField, 1, 10);
        Platform.runLater(() ->form.requestFocus());
        
        //refeesh button
        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #003366; -fx-text-fill:white; -fx-font-size: 16px;");
        refreshButton.setPrefWidth(100);
        refreshButton.setOnAction(e -> {
            updateProfileFields(nameField, bloodGroupField,ageField,
                    genderField,datePicker,contactField,emailField,
                    weightField,healthField, medicationField, noteField
                    );
            showAlert(Alert.AlertType.INFORMATION, "Profile Updated", "Profile information refreshed successfully!");
            
        });
        HBox buttonBox = new HBox(refreshButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding( new Insets(20, 0, 0, 0));
        
        
         VBox centerBox = new VBox(5,form, buttonBox);
        centerBox.setPadding(new Insets(20));
        display.setCenter(centerBox);
        
        display.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        form.setMaxWidth(Double.MAX_VALUE);
        form.setMaxHeight(Double.MAX_VALUE);
        BorderPane.setAlignment(centerBox, Pos.CENTER);
        
       
      
        
        
        return display;
    }
    

private void updateProfileFields(TextField nameField, TextField bloodGroupField, TextField ageField,
                                 TextField genderField, DatePicker datePicker, TextField contactField,
                                 TextField emailField, TextField weightField, TextField healthField,
                                 TextField medicationField, TextArea noteField) {
    if (currentDonor != null) {
        nameField.setText(currentDonor.getName());
        bloodGroupField.setText(currentDonor.getBloodGroup());
        ageField.setText(String.valueOf(currentDonor.getAge()));
        genderField.setText(currentDonor.getGender());
        datePicker.setValue(currentDonor.getAppointmentDate().toLocalDate());
        contactField.setText(currentDonor.getContact());
        emailField.setText(currentDonor.getEmail());
        weightField.setText(currentDonor.getWeight());
        healthField.setText(currentDonor.getHealthCondition());
        medicationField.setText(currentDonor.getMedication());
        noteField.setText(currentDonor.getNote());
    } else {
        
        nameField.clear();
        bloodGroupField.clear();
        ageField.clear();
        genderField.clear();
        datePicker.setValue(null);
        contactField.clear();
        emailField.clear();
        weightField.clear();
        healthField.clear();
        medicationField.clear();
        noteField.clear();
    }
}

private BorderPane viewDonorsPage() {
    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));

    Label title = new Label("All Registered Donors");
    title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #003366;");
    BorderPane.setAlignment(title, Pos.CENTER);
    root.setTop(title);

    TableView<Donor> donorTable = new TableView<>();
    donorTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

   
    TableColumn<Donor, String> nameCol = new TableColumn<>("Full Name");
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    nameCol.setPrefWidth(150);

    TableColumn<Donor, String> bloodCol = new TableColumn<>("Blood Group");
    bloodCol.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
    bloodCol.setPrefWidth(150);

    TableColumn<Donor, Integer> ageCol = new TableColumn<>("Age");
    ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
    ageCol.setPrefWidth(150);

    TableColumn<Donor, String> genderCol = new TableColumn<>("Gender");
    genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
    genderCol.setPrefWidth(150);

    TableColumn<Donor, String> contactCol = new TableColumn<>("Contact");
    contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
    contactCol.setPrefWidth(150);

    TableColumn<Donor, String> emailCol = new TableColumn<>("Email");
    emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    emailCol.setPrefWidth(150);

    TableColumn<Donor, String> weightCol = new TableColumn<>("Weight(kg)");
    weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
    weightCol.setPrefWidth(150);

    TableColumn<Donor, String> healthCol = new TableColumn<>("Health Condition");
    healthCol.setCellValueFactory(new PropertyValueFactory<>("healthCondition"));
    healthCol.setPrefWidth(150);

    TableColumn<Donor, String> medCol = new TableColumn<>("Medication");
    medCol.setCellValueFactory(new PropertyValueFactory<>("medication"));
    medCol.setPrefWidth(150);

    TableColumn<Donor, String> noteCol = new TableColumn<>("Note");
    noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
    noteCol.setPrefWidth(250);

    TableColumn<Donor, java.sql.Date> dateCol = new TableColumn<>("Appointment Date");
    dateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
    dateCol.setPrefWidth(150);
    
     wrapTextColumn(emailCol, 180);
    wrapTextColumn(noteCol, 250);
   

    // Add all columns to table
    donorTable.getColumns().addAll(nameCol, bloodCol, ageCol, genderCol, contactCol,
                                   emailCol, weightCol, healthCol, medCol, noteCol, dateCol);
    
    ScrollPane scrollPane = new ScrollPane(donorTable);
    scrollPane.setFitToWidth(false);
    scrollPane.setFitToHeight(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setPannable(true);

   
    List<Donor> donorList = database.getAllDonors();
    donorTable.setItems(FXCollections.observableArrayList(donorList));

    root.setCenter(scrollPane);

    return root;
}





public BorderPane searchDonorPage() {
    BorderPane searchPage = new BorderPane();
    searchPage.setPadding(new Insets(10));

   
    HBox searchBar = new HBox(10);
    searchBar.setAlignment(Pos.CENTER_LEFT);

    Label searchLabel = new Label("Search by Blood Group:");
    ComboBox<String> bloodGroupBox = new ComboBox<>();
    bloodGroupBox.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
    bloodGroupBox.setPromptText("Select Blood Group");

    Button searchButton = new Button("Search");
    searchButton.setStyle("-fx-background-color: #cc0000; -fx-text-fill: white;");

    searchBar.getChildren().addAll(searchLabel, bloodGroupBox, searchButton);
    searchBar.setPadding(new Insets(25));

    
    TableView<Donor> donorTable = new TableView<>();
    donorTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

    TableColumn<Donor, String> nameCol = new TableColumn<>("Full Name");
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    nameCol.setPrefWidth(150);

    TableColumn<Donor, String> bloodCol = new TableColumn<>("Blood Group");
    bloodCol.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
    bloodCol.setPrefWidth(150);

    TableColumn<Donor, Integer> ageCol = new TableColumn<>("Age");
    ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
    ageCol.setPrefWidth(150);

    TableColumn<Donor, String> genderCol = new TableColumn<>("Gender");
    genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
    genderCol.setPrefWidth(150);

    TableColumn<Donor, java.sql.Date> appointmentCol = new TableColumn<>("Appointment Date");
    appointmentCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
    appointmentCol.setPrefWidth(150);

    TableColumn<Donor, String> contactCol = new TableColumn<>("Contact");
    contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
    contactCol.setPrefWidth(150);

    TableColumn<Donor, String> emailCol = new TableColumn<>("Email");
    emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    emailCol.setPrefWidth(150);

    TableColumn<Donor, String> weightCol = new TableColumn<>("Weight");
    weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
    weightCol.setPrefWidth(150);

    TableColumn<Donor, String> healthCol = new TableColumn<>("Health Condition");
    healthCol.setCellValueFactory(new PropertyValueFactory<>("healthCondition"));
    healthCol.setMinWidth(150); 

    TableColumn<Donor, String> medCol = new TableColumn<>("Medication");
    medCol.setCellValueFactory(new PropertyValueFactory<>("medication"));
    medCol.setMinWidth(150);

    TableColumn<Donor, String> noteCol = new TableColumn<>("Note");
    noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
    noteCol.setMinWidth(200);

    donorTable.getColumns().addAll(nameCol, bloodCol, ageCol, genderCol, appointmentCol,
                                   contactCol, emailCol, weightCol, healthCol, medCol, noteCol);


    wrapTextColumn(emailCol, 180);
    wrapTextColumn(noteCol, 250);
    ScrollPane scrollPane = new ScrollPane(donorTable);
    scrollPane.setFitToWidth(false);
    scrollPane.setFitToHeight(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setPannable(true);

    
   
    searchPage.setTop(searchBar);
    searchPage.setCenter(scrollPane);

    
    searchButton.setOnAction(e -> {
        String bloodGroup = bloodGroupBox.getValue();
        if (bloodGroup != null && !bloodGroup.isEmpty()) {
            DatabaseConnection db = new DatabaseConnection();
            ObservableList<Donor> donors = db.getDonorsByBloodGroup(bloodGroup);
            donorTable.setItems(donors);
            db.closeConnection();
            
            if (donors == null|| donors.isEmpty())
            {
                donorTable.getItems().clear();
                showAlert(Alert.AlertType.INFORMATION, "Profile not found",
                        "There is no available donor for blood group: " + bloodGroup
                        );
            }
        } else {
            donorTable.getItems().clear();
            showAlert(Alert.AlertType.INFORMATION, "Profile not found", "There is no available donor for!");
        }
    });

    return searchPage;
}

private void wrapTextColumn(TableColumn<Donor, String> column, double prefwidth)
{
    column.setCellFactory(tc -> {
        TableCell<Donor, String> cell = new TableCell<>()
        {
            private final Text text = new Text();
            {
                text.wrappingWidthProperty().bind(column.widthProperty().subtract(10));
                setGraphic(text);
                
            }
            @Override
            protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty || item == null)
                {
                    text.setText(null);
                }
                else{
                    text.setText(item);
                }
            }
            
        };
        return cell;
    });
    column.setPrefWidth(prefwidth);
    column.setMinWidth(prefwidth);
}



    
    
    
}
