import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class ECommerceSystem {
    private List<User> users;
    private List<Product> products;
    private User currentUser;

    public ECommerceSystem() {
        users = new ArrayList<>();
        products = new ArrayList<>();
        currentUser = null;
        loadUsersFromFile();
        loadProductsFromFile();
    }

    private void loadUsersFromFile() {
        try {
            File file = new File("../data/user.txt");
            if (!file.exists()) return;

            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                User user = User.fromFileString(line);
                if (user != null) {
                    users.add(user);
                }
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void saveProductToFile(Product product) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("../data/product.txt", true))) {
            pw.println(product.toFileString());
        } catch (Exception e) {
            System.out.println("Error saving product: " + e.getMessage());
        }
    }

    private void loadProductsFromFile() {
        try {
            File file = new File("../data/product.txt");
            if (!file.exists()) return;

            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Product p = Product.fromFileString(line);
                if (p != null) {
                    products.add(p);
                }
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }

    public void startSystem() {
        System.out.println("Welcome to the E-Commerce System");
        mainMenu();
    }

    public void shutdownSystem() {
        System.out.println("System shutting down. Goodbye!");
    }

    public void mainMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
            System.out.println("4. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter role (customer/merchant): ");
                    String role = sc.nextLine();
                    if (registerUser(role)) {
                        System.out.println("Registration successful!");
                    } else {
                        System.out.println("Registration failed.");
                    }
                    break;
                case 2:
                    loginMenu();
                    break;
                case 3:
                    forgotPassword();
                    break;
                case 4:
                    shutdownSystem();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public void loginMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter username: ");
        String uname = sc.nextLine();
        Console console = System.console();
        String pass;
        if (console != null) {
            char[] passwordChars = console.readPassword("Enter password: ");
            pass = new String(passwordChars);
        } else {
            // Fallback if console is not available (e.g. IDEs)
            System.out.print("Enter password: ");
            pass = sc.nextLine();
        }

        for (User user : users) {
            if (user.login(uname, pass)) {
                currentUser = user;
                System.out.println("Login successful. Welcome " + currentUser.getUsername() + "!");
                if (currentUser instanceof Customer) {
                    customerMenu((Customer) currentUser);
                } else if (currentUser instanceof Merchant) {
                    merchantMenu((Merchant) currentUser);
                }
                return;
            }
        }
        System.out.println("Login failed. Invalid credentials.");
    }

    public boolean registerUser(String role) {
        Scanner sc = new Scanner(System.in);

        // validate role first
        if (!role.equalsIgnoreCase("customer") && !role.equalsIgnoreCase("merchant")) {
            System.out.println("Invalid role. Only 'customer' or 'merchant' roles are allowed.");
            return false;
        }

        System.out.print("Enter username: ");
        String uname = sc.nextLine();

        // vheck for duplicate username
        if (isUsernameTakenInFile(uname)) {
            System.out.println("Username already exists in file. Please choose a different username.");
            return false;
        }

        Console console = System.console();
        String pass;
        if (console != null) {
            char[] passwordChars = console.readPassword("Enter password: ");
            pass = new String(passwordChars);
        } else {
            System.out.print("Enter password: ");
            pass = sc.nextLine();
        }

        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter address: ");
        String address = sc.nextLine();

        User newUser = User.register(uname, pass, name, address, role);
        if (newUser != null) {
            users.add(newUser);
            saveUserToFile(newUser);
            System.out.println("User registered successfully.");
            return true;
        }

        return false;
    }

    private void saveOrderToFile(String orderDetails) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("../data/orders.txt", true))) {
            pw.println(orderDetails);
        } catch (Exception e) {
            System.out.println("Error saving order: " + e.getMessage());
        }
    }

    private void saveUserToFile(User user) {
        try {
            FileWriter fw = new FileWriter("../data/user.txt", true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(user.toFileString());
            pw.close();
        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    private void forgotPassword() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String uname = sc.nextLine();

        User user = null;
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(uname)) {
                user = u;
                break;
            }
        }

        if (user != null) {
            Console console = System.console();
            String newPass;
            if (console != null) {
                char[] passwordChars = console.readPassword("Enter new password: ");
                newPass = new String(passwordChars);
            } else {
                System.out.print("Enter new password: ");
                newPass = sc.nextLine();
            }

            user.setPassword(newPass); 
            updateUsersFile(); // Save updated list to file
            System.out.println("Password reset successful.");
        } else {
            System.out.println("User not found.");
        }
    }

    private boolean isUsernameTakenInFile(String username) {
        try (Scanner fileScanner = new Scanner(new File("../data/user.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(";");
                if (parts.length >= 2 && parts[1].equalsIgnoreCase(username)) {
                    return true; // Duplicate username found
                }
            }
        } catch (FileNotFoundException e) {
            // If the file doesn't exist yet, assume no users
        }
        return false; // Username is unique
    }


    private void updateUsersFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("../data/user.txt"))) {
            for (User u : users) {
                pw.println(u.toFileString());
            }
        } catch (Exception e) {
            System.out.println("Error updating users: " + e.getMessage());
        }
    }
    
    public void customerMenu(Customer customer) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. Browse products");
            System.out.println("2. Add product to cart");
            System.out.println("3. Update product quantity in cart");
            System.out.println("4. Remove product from cart");
            System.out.println("5. View cart");
            System.out.println("6. Checkout");
            System.out.println("7. Update delivery address");
            System.out.println("8. Logout");
            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    browseProducts();
                    break;
                case 2:
                    browseProducts();
                    System.out.print("Enter Product ID: ");
                    String pid = sc.nextLine();
                    Product p = findProductById(pid);  // make sure this loads from file

                    if (p != null) {
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        sc.nextLine();

                        if (qty <= p.getStockQuantity()) {
                            customer.addToCart(p, qty);
                            int newStock = p.getStockQuantity() - qty;
                            p.updateStock(newStock);  // update in object

                            updateProductStock(pid, newStock); // update in file

                            System.out.println("Added to cart.");
                        } else {
                            System.out.println("Not enough stock available.");
                        }
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;
                case 3:
                    System.out.println("\n--- Items in Cart ---");
                    if (customer.getCart().isEmpty()) {
                        System.out.println("Your cart is empty.");
                        break;
                    }

                    customer.viewItems();

                    System.out.print("Enter Product ID to update quantity: ");
                    String pidU = sc.nextLine();

                    Product pU = findProductById(pidU); // This should read from product.txt

                    if (pU != null && customer.getCart().contains(pU)) {
                        System.out.print("Enter new quantity: ");
                        int newQty = sc.nextInt();
                        sc.nextLine();

                        int currentQtyInCart = customer.getCart().getQuantityForProduct(pU);
                        int availableStock = pU.getStockQuantity() + currentQtyInCart;

                        if (newQty <= availableStock) {
                            int stockAdjustment = newQty - currentQtyInCart;

                            // Update stock before updating cart
                            pU.updateStock(pU.getStockQuantity() - stockAdjustment);

                            // Update cart
                            customer.updateCartItem(pU, newQty);

                            // Update stock in file
                            updateProductStock(pU.getProdID(), pU.getStockQuantity());

                            System.out.println("Cart updated.");
                        } else {
                            System.out.println("Not enough stock available.");
                        }
                    } else {
                        System.out.println("Product not found in cart.");
                    }
                    break;
                case 4:
                    System.out.println("\n--- Items in Cart ---");
                    if (customer.getCart().isEmpty()) {
                        System.out.println("Your cart is empty.");
                        break;
                    }

                    customer.viewItems();

                    System.out.print("Enter Product ID to remove: ");
                    String pidR = sc.nextLine();
                    Product pR = findProductById(pidR);
                    if (pR != null) {
                        customer.removeFromCart(pR);
                        System.out.println("Item removed.");
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;
                case 5:
                    System.out.println("--- Cart ---");
                    customer.viewItems();
                    break;
                case 6:
                    System.out.println("\n--- Order Summary ---");
                    System.out.println("Customer: " + customer.getName());
                    System.out.println("Delivery Address: " + customer.getAddress());
                    System.out.println("\nItems:");

                    CartItem[] items = customer.getCart().getItems();
                    double total = 0.0;

                    StringBuilder orderDetails = new StringBuilder();
                    orderDetails.append("Customer: ").append(customer.getName()).append("\n");
                    orderDetails.append("Address: ").append(customer.getAddress()).append("\n");
                    orderDetails.append("Items:\n");

                    for (CartItem item : items) {
                        double itemTotal = item.getProduct().getProdPrice() * item.getItemQuantity();
                        total += itemTotal;
                        String line = item.getProduct().getProdName() + " (x" + item.getItemQuantity() + ") - RM " + String.format("%.2f", itemTotal);
                        System.out.println(line);
                        orderDetails.append(line).append("\n");
                    }

                    System.out.println("Total: RM " + String.format("%.2f", total));
                    orderDetails.append("Total: RM ").append(String.format("%.2f", total)).append("\n");
                    orderDetails.append("----\n");

                    saveOrderToFile(orderDetails.toString());
                    System.out.println("Order placed! Thank you.");
                    customer.getCart().clear();

                    break;
                case 7:
                    System.out.print("Enter new address: ");
                    String newAddr = sc.nextLine();
                    customer.updateAddress(newAddr);
                    System.out.println("Address updated.");
                    break;
                case 8:
                    currentUser.logout();
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public void merchantMenu(Merchant merchant) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Merchant Menu ---");
            System.out.println("1. Add new product");
            System.out.println("2. View all products");
            System.out.println("3. Delete product");
            System.out.println("4. Update store address");
            System.out.println("5. Logout");
            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter product ID: ");
                    String id = sc.nextLine();
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Description: ");
                    String desc = sc.nextLine();
                    System.out.print("Price: ");
                    double price = sc.nextDouble();
                    System.out.print("Stock: ");
                    int stock = sc.nextInt();
                    sc.nextLine();
                    Product newProd = new Product(id, name, desc, price, stock);
                    if (merchant.addProduct(newProd)) {
                        saveProductToFile(newProd);
                        System.out.println("Product added.");
                    } else {
                        System.out.println("Failed to add product due to duplication.");
                    }
                    break;
                case 2:
                    browseProducts();
                    break;
                case 3:
                    System.out.print("Enter Product ID to delete: ");
                    String delId = sc.nextLine();
                    merchant.deleteProduct(delId);
                    break;
                case 4:
                    System.out.print("Enter new store address: ");
                    String newAddr = sc.nextLine();
                    merchant.setAddress(newAddr);
                    System.out.println("Address updated.");
                    break;
                case 5:
                    currentUser.logout();
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public void browseProducts() {
        File file = new File("../data/product.txt");

        if (!file.exists()) {
            System.out.println("No products available.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("=== Available Products ===");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length >= 5) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String desc = parts[2].trim();
                    String price = parts[3].trim();
                    String stock = parts[4].trim();

                    System.out.println("ID          : " + id);
                    System.out.println("Name        : " + name);
                    System.out.println("Description : " + desc);
                    System.out.println("Price       : RM " + String.format("%.2f", Double.parseDouble(price)));
                    System.out.println("Stock       : " + stock + " units");
                    System.out.println("------------------------------------------");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading products: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing product price: " + e.getMessage());
        }
    }

    public Product findProductById(String productId) {
        File file = new File("../data/product.txt");
        if (!file.exists()) {
            System.out.println("Product file not found.");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length >= 5 && parts[0].trim().equalsIgnoreCase(productId)) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String desc = parts[2].trim();
                    double price = Double.parseDouble(parts[3].trim());
                    int stock = Integer.parseInt(parts[4].trim());
                    return new Product(id, name, desc, price, stock);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading product file: " + e.getMessage());
        }

        return null; // If not found
    }

    public void updateProductStock(String productId, int newStock) {
        File file = new File("../data/product.txt");
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length >= 5 && parts[0].equalsIgnoreCase(productId)) {
                    // Modify stock
                    parts[4] = String.valueOf(newStock);
                    line = String.join(";", parts); // Rebuild the line
                }
                lines.add(line);
            }

            // Overwrite file with updated lines
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
                for (String l : lines) {
                    writer.println(l);
                }
            }

            System.out.println("Stock updated successfully.");
        } catch (IOException e) {
            System.out.println("Error updating product stock: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        ECommerceSystem system = new ECommerceSystem();
        system.startSystem();
    }
}
