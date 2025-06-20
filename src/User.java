import java.io.FileWriter;
import java.io.PrintWriter;

// Declared as abstract because never want to create a plain User object directly, only Customer or Merchant
public abstract class User {
    // protected makes these fields accessible to subclasses
    protected String username;
    protected String password;
    protected String name;
    protected String address;

    public User(String username, String password, String name, String address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
    }

    public boolean login(String uname, String pass) {
        return this.username.equals(uname) && this.password.equals(pass);
    }

    public void logout() {
        System.out.println(username + " logged out.");
    }

    public static User register(String uname, String pass, String name, String address, String role) {
        // Validate all required fields
        if (uname == null || uname.trim().isEmpty())
            throw new IllegalArgumentException("Username cannot be null or empty.");
        if (pass == null || pass.trim().isEmpty())
            throw new IllegalArgumentException("Password cannot be null or empty.");
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty.");
        if (address == null || address.trim().isEmpty())
            throw new IllegalArgumentException("Address cannot be null or empty.");
        if (role == null || role.trim().isEmpty())
            throw new IllegalArgumentException("Role cannot be null or empty.");

        // Create user based on role
        if (role.equalsIgnoreCase("customer")) {
            return new Customer(uname, pass, name, address);
        } else if (role.equalsIgnoreCase("merchant")) {
            return new Merchant(uname, pass, name, address);
        } else {
            throw new IllegalArgumentException("Invalid role: must be 'customer' or 'merchant'.");
        }
    }

    public String toFileString() {
        return this.getClass().getSimpleName() + ";" + username + ";" + password + ";" + name + ";" + address;
    }

    public static User fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null; // Skip invalid input
        }

        String[] parts = line.split(";");
        if (parts.length < 5) return null;

        String role = parts[0];
        String uname = parts[1];
        String pass = parts[2];
        String name = parts[3];
        String address = parts[4];

        if (role.equalsIgnoreCase("Customer")) {
            return new Customer(uname, pass, name, address);
        } else if (role.equalsIgnoreCase("Merchant")) {
            return new Merchant(uname, pass, name, address);
        }

        return null; // Unknown role
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}
