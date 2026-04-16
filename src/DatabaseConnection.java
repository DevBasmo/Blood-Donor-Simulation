import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DatabaseConnection {
    
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/blood_donor_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USERNAME= "root";
    static final String PASSWORD = "familyme1234;";
    
    private Connection connection = null;
    private PreparedStatement login = null;
    private PreparedStatement insertDonor = null;
    private PreparedStatement selectAllDonors = null;
    private PreparedStatement getDonorsByBloodGroup = null;
    
    
    
    public DatabaseConnection()
    {
        
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            
            login = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ? AND role = ?");
            
                        
            insertDonor = connection.prepareStatement(
                "INSERT INTO donors (full_name, blood_group, age, gender, appointment_date, contact, email, weight, health_condition, medication, note) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            
            selectAllDonors  = connection.prepareStatement("SELECT * FROM donors");
            
            getDonorsByBloodGroup = connection.prepareStatement("SELECT * FROM donors WHERE blood_group = ?");

            
            
            
        }
        
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public boolean validateLogin(String username, String password, String role)
    {
        try
        {
            login.setString(1, username);
            login.setString(2, password);
            login.setString(3, role);
            
            ResultSet result = login.executeQuery();
            
            if ( result.next())
            {
                return true;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean addDonor(String name, String bloodGroup, int age, 
            String gender,Date appointmentDate, String contact, String email, String weight,
            String healthCondition, String medication, String note)
    {
        try
        {
            insertDonor.setString(1, name);
            insertDonor.setString(2, bloodGroup);
            insertDonor.setInt(3, age);
            insertDonor.setString(4, gender);
            insertDonor.setDate(5, appointmentDate);
            insertDonor.setString(6, contact);
            insertDonor.setString(7, email);
            insertDonor.setString(8, weight);
            insertDonor.setString(9, healthCondition);
            insertDonor.setString(10, medication);
            insertDonor.setString(11, note);
            
            int rows = insertDonor.executeUpdate();
            
            return rows > 0;
        }
        
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return false;
        
        
    }
    
    
    
    public List<Donor> getAllDonors()
    {
        List<Donor> donors = new ArrayList<>();
        
        try
        {
            ResultSet result = selectAllDonors.executeQuery();
            
            while(result.next())
            {
                Donor donor = new Donor(
                        
                        result.getString("full_name"),
                        result.getString("blood_group"),
                        result.getInt("age"),
                        result.getString("gender"),
                        result.getString("contact"),
                        result.getString("email"),
                        result.getString("weight"),
                        result.getString("health_condition"),
                        result.getString("medication"),
                        result.getString("note"),
                        result.getDate("appointment_date")
                
                );
                donors.add(donor);
            }
            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return donors;
        
    }
    
    
    public ObservableList<Donor> getDonorsByBloodGroup(String bloodGroup)
    {
        ObservableList<Donor > donors = FXCollections.observableArrayList();
        
        try
        {
            getDonorsByBloodGroup.setString(1, bloodGroup);
            ResultSet result = getDonorsByBloodGroup.executeQuery();
            
            while ( result.next())
            {
                donors.add(new Donor(
                        result.getString("full_name"),
                        result.getString("blood_group"),
                        result.getInt("age"),
                        result.getString("gender"),
                        result.getString("contact"),
                        result.getString("email"),
                        result.getString("weight"),
                        result.getString("health_condition"),
                        result.getString("medication"),
                        result.getString("note"),
                        result.getDate("appointment_date")
                ));
            }
        }
        
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        
        return donors;
        
    }
    
    
    
    
    public void closeConnection()
    {
        try
        {
            if(login != null)login.close();
            if(insertDonor != null) insertDonor.close();
            if(connection != null) connection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
}
