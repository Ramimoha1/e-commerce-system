import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users;

    public UserManager() {
        users = new ArrayList<>();
    }

    public void loadFromFile(String filename) {
        users.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                User u = User.fromFileString(line);
                if (u != null)
                    users.add(u);
            }
        } catch (Exception e) {
            System.out.println("Unable to load users: " + e.getMessage());
        }
    }

    public void saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (User u : users) {
                pw.println(u.toFileString());
            }
        } catch (Exception e) {
            System.out.println("Unable to save users: " + e.getMessage());
        }
    }

    // to add new user in the list
    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUserByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }
}
