public class User {
    private String name;
    private String username;
    private String password;
    private String address;

    public User(String name, String username, String password, String address) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;
    }

    // Getters for the child class to access (especially name)
    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String newAddress) {
        this.address = newAddress;
    }

    public boolean register(String name, String username, String password, String address) {
        this.name = name;
        this.password = password;
        this.username = username;
        this.address = address;

        return true;
    }

    // maybe logstatus should be global variable.
    public boolean login (String uname, String pass) {
        boolean logStatus = (uname.equals(username) && pass.equals(password));
        return logStatus;
    }

    public boolean logout () {
        return false;
    }

    /*
    public String toFileString() {

    }

    public void fromFileString (String fileline) {
    
    }
     */
}
