package models;
import models.UserType;

public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String zipCode;
    private String streetName;
    private String password;
    private int houseNr;
    private int phoneNr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAdress() {
        return emailAddress;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAddress = emailAdress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getHouseNr() {
        return houseNr;
    }

    public void setHouseNr(int houseNr) {
        this.houseNr = houseNr;
    }

    public int getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(int phoneNr) {
        this.phoneNr = phoneNr;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    private models.UserType userType;

    public User(String ID, String FirstName, String LastName, String EmailAdress, String ZipCode,String StreetName , int HouseNr, int PhoneNr, int userType)
    {
        this.id = ID;
        this.firstName = FirstName;
        this.lastName = LastName;
        this.emailAddress = EmailAdress;
        this.zipCode = ZipCode;
        this.streetName = StreetName;
        this.houseNr = HouseNr;
        this.phoneNr = PhoneNr;
        this.userType = UserType.values()[userType];
    }

    public User(String email, String password) {
        this.emailAddress = email;
        this.password = password;
    }

    public User() {}

    public UserType getUserType()
    {
        return this.userType;
    }

    public void changeUserType(UserType type)
    {
        this.userType = type;
    }
}

