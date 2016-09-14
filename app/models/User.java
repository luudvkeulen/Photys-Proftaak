package models;
import models.UserType;

/**
 * Created by Thijs on 14-9-2016.
 */

public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String emailAdress;
    private String zipCode;
    private String streetName;
    private int houseNr;
    private int phoneNr;

    private models.UserType userType;

    public User(String ID, String FirstName, String LastName, String EmailAdress, String ZipCode,String StreetName , int HouseNr, int PhoneNr, int userType)
    {
        this.id = ID;
        this.firstName = FirstName;
        this.lastName = LastName;
        this.emailAdress = EmailAdress;
        this.zipCode = ZipCode;
        this.streetName = StreetName;
        this.houseNr = HouseNr;
        this.phoneNr = PhoneNr;
        this.userType = UserType.values()[userType];
    }

    public UserType getUserType()
    {
        return this.userType;
    }

    public void changeUserType(UserType type)
    {
        this.userType = type;
    }
}

