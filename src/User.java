// Declared as abstract because never want to create a plain User object directly, only Customer or Merchant
public abstract class User {
    // protected makes these fields accessible to subclasses
    protected String username;
    protected String password;
    protected String name;
    protected String[] address;   // regular array of size 2, index 0 - postal code, index 1 - location

    public User(String username, String password, String name, String[] address) {
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

    public static User register(String uname, String pass, String name, String[] address, String role) {
        // Validate all required fields
        String fullAddress = address[0] + '|' + address[1];
        if (uname == null || uname.trim().isEmpty())
            throw new IllegalArgumentException("Username cannot be null or empty.");
        if (pass == null || pass.trim().isEmpty())
            throw new IllegalArgumentException("Password cannot be null or empty.");
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty.");
        if (fullAddress == null || fullAddress.trim().isEmpty())
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
        String addressStr = address[0] + "|" + address[1];
        return this.getClass().getSimpleName() + ";" + username + ";" + password + ";" + name + ";" + addressStr;
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
        String[] addressnew = parts[4].split("\\|");

        if (role.equalsIgnoreCase("Customer")) {
            return new Customer(uname, pass, name, addressnew);
        } else if (role.equalsIgnoreCase("Merchant")) {
            return new Merchant(uname, pass, name, addressnew);
        }

        return null; // Unknown role
    }

    public abstract void viewDashboard();   // Polymorphism

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
        String newaddress = address[0] + " , " + address[1];
        return newaddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String[] address) {
        this.address = address;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}