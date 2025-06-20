import java.io.*;
import java.util.*;

public class UserManager {
    private List<User> users = new ArrayList<>();

    // Load users from a file
    public void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                User user = User.fromFileString(line); // Adds valid users to the list
                if (user != null) users.add(user);
            }
        } catch (IOException e) {
            System.out.println("Error loading user file: " + e.getMessage());
        }
    }

    // Save users to a file
    public void saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (User user : users) {
                pw.println(user.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving user file: " + e.getMessage());
        }
    }

    // Add user only if username is unique
    public void addUser(User user) {
        if (getUserByUsername(user.getUsername()) == null) {
            users.add(user);
            System.out.println("User added successfully.");
        } else {
            System.out.println("Username already exists. Choose a different one.");
        }
    }

    // Remove user by username
    public boolean removeUser(String username) {
        return users.removeIf(u -> u.getUsername().equals(username));
    }

    // Find a user by username
    public User getUserByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    // Authenticate user credentials
    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.login(username, password)) {
                return user;    // Returns the user if credentials match.
            }
        }
        return null;
    }

    // Optional: Get all users
    public List<User> getAllUsers() {
        return users;
    }
}

