package e.bartek.logowanie;

/**
 * Created by Bartek on 17.08.2018.
 */

public class UserInformation {
    public String name;
    public String address;
    public String id;
    public String email;

    public UserInformation(){

    }

    public UserInformation(String name, String address, String id, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
