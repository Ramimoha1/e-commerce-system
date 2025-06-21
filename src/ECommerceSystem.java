import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class ECommerceSystem {
    private List<User> users;
    private List<Product> products;
    private List<Product> orders;
    private User currentUser;

    public Scanner sc;

    public ECommerceSystem() {
        users = new ArrayList<>();
        products = new ArrayList<>();
        currentUser = null;
        loadUsersFromFile();
        loadProductsFromFile();
        sc = new Scanner(System.in);
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
        clearTerminal();
        System.out.println("Welcome to the E-Commerce System");
        mainMenu();
    }

    public void shutdownSystem() {
        clearTerminal();
        System.out.println("System shutting down. Goodbye!");
    }
    
    public void clearTerminal()
    {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    public void registerMenu()
    {
        clearTerminal();
        System.out.println("\n--- Register ---");
        System.out.print("Enter role (customer/merchant): ");
        String role = sc.nextLine();
        if (registerUser(role)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("\nTo try again press 1 else 2"); 
            System.out.print("Choice: ");  
            int choice = sc.nextInt();
            sc.nextLine();
            while(choice != 1 && choice != 2)
            {
                System.out.println("Invalid choice! Please enter 1 or 2");
                System.out.println("To try again press 1 else 2"); 
                System.out.print("Choice: ");  
                choice = sc.nextInt();
                sc.nextLine();
            }
            
            if(choice == 1)
            {
                registerMenu();
            }
            else
            {
                mainMenu();
            }
        }
    }

    public void mainMenu() {
        while (true) {
            clearTerminal();
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
            System.out.println("4. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            while(choice < 1 || choice > 4)
            {
                System.err.println("Invalid choice! Please enter a value from 1 to 4");
                System.out.print("Enter choice: ");
                choice = sc.nextInt();
                sc.nextLine();
            }
            
            switch (choice) {
                case 1: 
                    registerMenu();
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
            }
        }
    }

    public void loginMenu() {
        clearTerminal();
        System.out.println("\n--- Log In ---");
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
            sc.nextLine();
            sc.nextLine();
            pass = sc.nextLine();
        }

        for (User user : users) {
            if (user.login(uname, pass)) {
                currentUser = user;
                clearTerminal();
                currentUser.viewDashboard();
                if (currentUser instanceof Customer) {
                    customerMenu((Customer) currentUser);
                } else if (currentUser instanceof Merchant) {
                    merchantMenu((Merchant) currentUser);
                }
                return;
            }
        }
        
        System.out.println("Login failed. Invalid credentials.");
        System.out.println("To try again press 1 else 2"); 
        System.out.print("Choice: ");  
        int choice = sc.nextInt();
        sc.nextLine();
        while(choice != 1 && choice != 2)
        {
            System.out.println("Invalid choice! Please enter 1 or 2");
            System.out.println("To try again press 1 else 2"); 
            System.out.print("Choice: ");  
            choice = sc.nextInt();
            sc.nextLine();
        }
        
        if(choice == 1)
        {
            loginMenu();
        }
        else
        {
            mainMenu();
        }
    }

    public boolean registerUser(String role) {
        if (!role.equalsIgnoreCase("customer") && !role.equalsIgnoreCase("merchant")) {
            System.out.println("Invalid role. Only 'customer' or 'merchant' roles are allowed.");
            return false;
        }

        System.out.print("Enter username: ");
        String uname = sc.nextLine();

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
        clearTerminal();
        System.out.println("\n--- Change Password ---");
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
            updateUsersFile();
            System.out.println("Password reset successful.");
            System.out.println("Press any key to continue...");
            sc.nextLine();
        } else {
            System.out.println("User not found.");
            System.out.println("Press any key to continue...");
            sc.nextLine();
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
        }
        return false;
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
    while (true) {
        clearTerminal();
        System.out.println("\n--- Customer Menu ---");
        System.out.println("1. Browse products");
        System.out.println("2. Manage cart");
        System.out.println("3. Checkout");
        System.out.println("4. View Current Orders");
        System.out.println("5. Update delivery address");
        System.out.println("6. Logout");
        System.out.print("Choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
        
        while(choice < 1 || choice > 6) {
            System.err.println("Invalid choice! Please enter a value from 1 to 6");
            System.out.print("Choice: ");
            choice = sc.nextInt();
            sc.nextLine();
        }

        switch (choice) {
            case 1:
                clearTerminal();
                browseProducts();
                System.out.println("Press any key to continue...");
                sc.nextLine();
                break;
            case 2:
                manageCartMenu(customer);
                break;
            case 3:
                clearTerminal();
                if (customer.getCart().getItems().length == 0) {
                    System.out.println("Your cart is empty. Please add some items first.");
                    System.out.println("Press any key to continue...");
                    sc.nextLine();
                    break;
                }
                
                System.out.println("\n--- Order Summary ---");
                System.out.println("Time: " + LocalDateTime.now());
                System.out.println("Customer: " + customer.getName());
                System.out.println("Delivery Address: " + customer.getAddress());
                System.out.println("\nItems:");
                
                CartItem[] items = customer.getCart().getItems();
                double total = 0.0;

                // Create new order
                Order newOrder = new Order(Order.getNextOrderId(), customer);

                StringBuilder orderDetails = new StringBuilder();
                orderDetails.append("Customer: ").append(customer.getName()).append("\n");
                orderDetails.append("Address: ").append(customer.getAddress()).append("\n");
                orderDetails.append("Order ID: ").append(newOrder.getOrderId()).append("\n");
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
                orderDetails.append("Date: ").append(LocalDateTime.now()).append("\n");
                orderDetails.append("Status: ").append(newOrder.getStatus()).append("\n");
                orderDetails.append("----\n");

                // Confirm order
                System.out.print("Confirm order? (y/n): ");
                String confirm = sc.nextLine();
                if (confirm.toLowerCase().startsWith("y")) {
                    saveOrderToFile(orderDetails.toString());
                    saveOrderToDatabase(newOrder);
                    System.out.println("Order placed! Order ID: " + newOrder.getOrderId());
                    customer.getCart().clear();
                } else {
                    System.out.println("Order cancelled.");
                }
                
                System.out.println("Press any key to continue...");
                sc.nextLine();
                break;
            case 4:
                clearTerminal();
                viewCustomerOrders(customer);
                System.out.println("Press any key to continue...");
                sc.nextLine();
                break;
            case 5:
                clearTerminal();
                System.out.print("Enter new address: ");
                String newAddr = sc.nextLine();
                customer.updateAddress(newAddr);
                System.out.println("Address updated.");
                System.out.println("Press any key to continue...");
                sc.nextLine();
                break;
            case 6:
                currentUser.logout();
                currentUser = null;
                return;
        }
    }
}

// Helper method to view customer's orders
private void viewCustomerOrders(Customer customer) {
    try {
        File file = new File("../data/Order.txt");
        if (!file.exists()) {
            System.out.println("No orders found.");
            return;
        }
        
        boolean foundOrders = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("\n--- Your Orders ---");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5 && parts[1].equals(customer.getName())) {
                    foundOrders = true;
                    System.out.println("Order ID: " + parts[0]);
                    System.out.println("Total: RM " + String.format("%.2f", Double.parseDouble(parts[2])));
                    System.out.println("Date: " + parts[3]);
                    System.out.println("Status: " + parts[4]);
                    System.out.println("----");
                }
            }
        }
        
        if (!foundOrders) {
            System.out.println("You have no orders yet.");
        }
    } catch (IOException e) {
        System.err.println("Error reading orders: " + e.getMessage());
    }
}

// Helper method to save order to database
private void saveOrderToDatabase(Order order) {
    try {
        File file = new File("../data/Order.txt");
        file.getParentFile().mkdirs(); // Create directory if it doesn't exist
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(order.toFileString());
            writer.newLine();
        }
    } catch (IOException e) {
        System.err.println("Error saving order to database: " + e.getMessage());
    }
}

    public void manageCartMenu(Customer customer) {
        while (true) {
            clearTerminal();
            System.out.println("\n--- Manage Cart ---");
            System.out.println("1. Add product to cart");
            System.out.println("2. Update product quantity in cart");
            System.out.println("3. Remove product from cart");
            System.out.println("4. View cart");
            System.out.println("5. Back to customer menu");
            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            
            while(choice < 1 || choice > 5)
            {
                System.err.println("Invalid choice! Please enter a value from 1 to 5");
                System.out.print("Choice: ");
                choice = sc.nextInt();
                sc.nextLine();
            }

            switch (choice) {
                case 1:
                    clearTerminal();
                    browseProducts();
                    System.out.print("Enter Product ID: ");
                    String pid = sc.nextLine();
                    Product p = findProductById(pid);

                    if (p != null) {
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        sc.nextLine();

                        if (qty <= p.getStockQuantity()) {
                            customer.addToCart(p, qty);
                            int newStock = p.getStockQuantity() - qty;
                            p.updateStock(newStock);

                            updateProductStock(pid, newStock);

                            System.out.println("Added to cart.");
                        } else {
                            System.out.println("Not enough stock available.");
                        }
                    } else {
                        System.out.println("Product not found.");
                    }
                    System.out.println("Press any key to continue...");
                    sc.nextLine();
                    break;
                case 2:
                    clearTerminal();
                    System.out.println("\n--- Items in Cart ---");
                    if (customer.getCart().isEmpty()) {
                        System.out.println("Your cart is empty.");
                        System.out.println("Press any key to continue...");
                        sc.nextLine();
                        break;
                    }

                    customer.viewItems();

                    System.out.print("Enter Product ID to update quantity: ");
                    String pidU = sc.nextLine();

                    Product pU = findProductById(pidU);

                    if (pU != null && customer.getCart().contains(pU)) {
                        System.out.print("Enter new quantity: ");
                        int newQty = sc.nextInt();
                        sc.nextLine();

                        int currentQtyInCart = customer.getCart().getQuantityForProduct(pU);
                        int availableStock = pU.getStockQuantity() + currentQtyInCart;

                        if (newQty <= availableStock) {
                            int stockAdjustment = newQty - currentQtyInCart;

                            pU.updateStock(pU.getStockQuantity() - stockAdjustment);

                            customer.updateCartItem(pU, newQty);

                            updateProductStock(pU.getProdID(), pU.getStockQuantity());

                            System.out.println("Cart updated.");
                        } else {
                            System.out.println("Not enough stock available.");
                        }
                    } else {
                        System.out.println("Product not found in cart.");
                    }
                    System.out.println("Press any key to continue...");
                    sc.nextLine();
                    break;
                case 3:
                    clearTerminal();
                    System.out.println("\n--- Items in Cart ---");
                    if (customer.getCart().isEmpty()) {
                        System.out.println("Your cart is empty.");
                        System.out.println("Press any key to continue...");
                        sc.nextLine();
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
                    System.out.println("Press any key to continue...");
                    sc.nextLine();
                    break;
                case 4:
                    clearTerminal();
                    System.out.println("--- Cart ---");
                    customer.viewItems();
                    System.out.println("Press any key to continue...");
                    sc.nextLine();
                    break;
                case 5:
                    return; // Return to customer menu
            }
        }
    }

   public void merchantMenu(Merchant merchant) {
    while (true) {
        clearTerminal();
        System.out.println("\n--- Merchant Menu ---");
        System.out.println("1. Add new product");
        System.out.println("2. View all products");
        System.out.println("3. Delete product");
        System.out.println("4. View Orders");
        System.out.println("5. Update Orders");
        System.out.println("6. Update store address");
        System.out.println("7. Logout");
        System.out.print("Choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
        
        while(choice < 1 || choice > 7) {
            System.err.println("Invalid choice! Please enter a value from 1 to 7");
            System.out.print("Choice: ");
            choice = sc.nextInt();
            sc.nextLine();
        }

        switch (choice) {
            case 1:
                clearTerminal();
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
                System.out.println("Press any key to continue...");
                sc.nextLine();
                break;
            case 2:
                clearTerminal();
                browseProducts();
                System.out.println("Press any key to continue...");
                sc.nextLine();
                break;
            case 3:
                clearTerminal();
                browseProducts();
                System.out.print("Enter Product ID to delete: ");
                String delId = sc.nextLine();
                merchant.deleteProduct(delId);
                System.out.println("Press any key to continue...");
                sc.nextLine();
                break;
            case 4:
                clearTerminal();
                displayAllOrders();
                System.out.println("Press any key to continue...");
                sc.nextLine();
                break;
            case 5:
                clearTerminal();
                manageOrderStatus();
                System.out.println("Press any key to continue...");
                sc.nextLine();
                break;
            case 6:
                clearTerminal();
                System.out.print("Enter new store address: ");
                String newAddr = sc.nextLine();
                merchant.setAddress(newAddr);
                System.out.println("Address updated.");
                System.out.println("Press any key to continue...");
                sc.nextLine();
                break;
            case 7:
                currentUser.logout();
                currentUser = null;
                return;
        }
    }
}

// Method to display all orders
private void displayAllOrders() {
    try {
        File file = new File("../data/Order.txt");
        if (!file.exists()) {
            System.out.println("No orders found.");
            return;
        }
        
        System.out.println("\n--- All Orders ---");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int orderCount = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    orderCount++;
                    System.out.println("Order ID: " + parts[0]);
                    System.out.println("Customer: " + parts[1]);
                    System.out.println("Total Amount: RM " + String.format("%.2f", Double.parseDouble(parts[2])));
                    System.out.println("Date/Time: " + parts[3]);
                    System.out.println("Status: " + parts[4]);
                    System.out.println("----");
                }
            }
            
            if (orderCount == 0) {
                System.out.println("No orders found.");
            } else {
                System.out.println("Total orders: " + orderCount);
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading orders: " + e.getMessage());
    }
}

// Method to manage order status
private void manageOrderStatus() {
    try {
        File file = new File("../data/Order.txt");
        if (!file.exists()) {
            System.out.println("No orders found.");
            return;
        }
        
        List<Order> orders = new ArrayList<>();
        
        // Loading all orders
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    // Create a temporary order object for display
                    Order order = new Order(Integer.parseInt(parts[0]), parts[1], new ShoppingCart());
                    order.setTotalAmount(Double.parseDouble(parts[2])) ;
                    order.setDatetime(LocalDateTime.parse(parts[3])); 
                    order.setStatus(OrderStatus.valueOf(parts[4]));
                    orders.add(order);
                }
            }
        }
        
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        
        // Display orders
        System.out.println("\n--- Order Status Management ---");
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            System.out.println((i + 1) + ". Order ID: " + order.getOrderId() + 
                             " | Customer: " + order.getCustomerName() + 
                             " | Status: " + order.getStatus() + 
                             " | Total: RM " + String.format("%.2f", order.getTotalAmount()));
        }
        
        System.out.print("Select order to update (1-" + orders.size() + "), or 0 to cancel: ");
        int choice = sc.nextInt();
        sc.nextLine();
        
        if (choice > 0 && choice <= orders.size()) {
            Order selectedOrder = orders.get(choice - 1);
            
            System.out.println("\nSelected Order:");
            System.out.println("Order ID: " + selectedOrder.getOrderId());
            System.out.println("Customer: " + selectedOrder.getCustomerName());
            System.out.println("Current Status: " + selectedOrder.getStatus());
            
            // Show status options
            System.out.println("\nAvailable Status Options:");
            OrderStatus[] statuses = OrderStatus.values();
            for (int i = 0; i < statuses.length; i++) {
                System.out.println((i + 1) + ". " + statuses[i].getDisplayName());
            }
            
            System.out.print("Select new status (1-" + statuses.length + "): ");
            int statusChoice = sc.nextInt();
            sc.nextLine();
            
            if (statusChoice >= 1 && statusChoice <= statuses.length) {
                OrderStatus newStatus = statuses[statusChoice - 1];
                selectedOrder.changeStatus(newStatus);
                
                // Update the file
                updateOrderInFile(selectedOrder);
                System.out.println("Order status updated successfully!");
            } else {
                System.out.println("Invalid status choice.");
            }
        } else if (choice != 0) {
            System.out.println("Invalid order selection.");
        }
        
    } catch (IOException e) {
        System.err.println("Error managing orders: " + e.getMessage());
    }
}

// Helper method to update order in file
private void updateOrderInFile(Order updatedOrder) {
    try {
        File file = new File("../data/Order.txt");
        List<String> lines = new ArrayList<>();
        
        // Read all lines and update the specific order
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 0 && Integer.parseInt(parts[0]) == updatedOrder.getOrderId()) {
                    // Replace with updated order data
                    lines.add(updatedOrder.toFileString());
                } else {
                    // Keep other orders unchanged
                    lines.add(line);
                }
            }
        }
        
        // Write all lines back to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
        
    } catch (IOException e) {
        System.err.println("Error updating order file: " + e.getMessage());
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

        return null;
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

        } catch (IOException e) {
            System.out.println("Error updating product stock: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ECommerceSystem system = new ECommerceSystem();
        system.startSystem();
    }
}
