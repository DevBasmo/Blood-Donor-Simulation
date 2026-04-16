public class Donor {
    private String name;
    private String bloodGroup;
    private int age;
    private String gender;
    private String contact;
    private String email;
    private String weight;
    private String healthCondition;
    private String medication;
    private String note;
    private java.sql.Date appointmentDate;

    // Constructor
    public Donor(String name, String bloodGroup, int age, String gender, String contact, 
                 String email, String weight, String healthCondition, 
                 String medication, String note, java.sql.Date appointmentDate) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.age = age;
        this.gender = gender;
        this.contact = contact;
        this.email = email;
        this.weight = weight;
        this.healthCondition = healthCondition;
        this.medication = medication;
        this.note = note;
        this.appointmentDate = appointmentDate;
    }

  
    public String getName() { return name; }
    public String getBloodGroup() { return bloodGroup; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getContact() { return contact; }
    public String getEmail() { return email; }
    public String getWeight() { return weight; }
    public String getHealthCondition() { return healthCondition; }
    public String getMedication() { return medication; }
    public String getNote() { return note; }
    public java.sql.Date getAppointmentDate() { return appointmentDate; }
}
