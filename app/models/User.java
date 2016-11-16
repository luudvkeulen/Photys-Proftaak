package models;
import models.UserType;

public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String zipCode;
    private String streetName;
    private String password;
    private int houseNr;
    private int phoneNr;
    private boolean isBanned;
    private String houseNr;
    private String phoneNr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getHouseNr() {
        return houseNr;
    }

    public void setHouseNr(String houseNr) {
        this.houseNr = houseNr;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public boolean GetIsBanned(){return isBanned;}

    public void setUserType(UserType userType) {
        this.userType = userType;}

    private models.UserType userType;

    public User(int ID, String FirstName, String LastName, String EmailAdress, String ZipCode,String StreetName , int HouseNr, int PhoneNr, int userType, int isBanned)
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
        this.isBanned = (isBanned != 0);
    }

    public User(int id, String firstName, String lastName, String emailAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public User(String email, String password) {
        this.emailAddress = email;
        this.password = password;
    }

    public User(String email, String firstName, String lastName, String zipcode, String street, String housenr, String phonenr, int type) {
        this.emailAddress = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.zipCode = zipcode;
        this.streetName = street;
        this.houseNr = housenr;
        this.phoneNr = phonenr;
        this.userType = UserType.values()[type];
    }

    public User() {
    }

    public UserType getUserType() {
        return this.userType;
    }

    public void changeUserType(UserType type) {
        this.userType = type;
    }

    public void ChangeUserBan(boolean value)
    {
        this.isBanned = value;
    }
}

