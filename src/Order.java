import java.time.LocalDateTime;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Enum for Order Status
enum OrderStatus {
    PENDING("Pending"),
    PROCESSING("Processing"), 
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");
    
    private final String displayName;
    
    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}

public class Order {
    // Attributes
    private String orderId;
    private String customerId;
    private ShoppingCart cart;
    private double totalAmount;
    private LocalDateTime datetime;
    private OrderStatus status;
    
    // Constructor
    public Order(String orderId, String customerId, ShoppingCart cart) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.cart = cart;
        this.datetime = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
        this.totalAmount = cart.calculateTotal();
    }
    
    // Getter methods
    public String getOrderId() {
        return orderId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public ShoppingCart getCart() {
        return cart;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public LocalDateTime getDatetime() {
        return datetime;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void changeStatus() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Current Status: " + status);
        System.out.println("Available Status Options:");
        
    
        OrderStatus[] statuses = OrderStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + ". " + statuses[i].getDisplayName());
        }
        
        System.out.print("Select new status (1-" + statuses.length + "): ");
        
        try {
            int choice = scanner.nextInt();
            if (choice >= 1 && choice <= statuses.length) {
                OrderStatus oldStatus = this.status;
                this.status = statuses[choice - 1];
                System.out.println("Status updated to: " + this.status);
                
                // Update in database
                updateStatusInDatabase();
                
            } else {
                System.out.println("Invalid choice. Status not changed.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Status not changed.");
        }
    }
    
    
    
    // Update this order's status in the database file
    private void updateStatusInDatabase() {
        try {
            File file = new File("../data/Order.txt");
            List<String> lines = new ArrayList<>();
            
            // Read all lines from file
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length > 0 && parts[0].equals(this.orderId)) {
                            // Replace this order's line with updated data
                            lines.add(this.toFileString());
                        } else {
                            // Keep other orders unchanged
                            lines.add(line);
                        }
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
            
            System.out.println("Database updated successfully.");
            
        } catch (IOException e) {
            System.err.println("Error updating database: " + e.getMessage());
        }
    }
    
    public void displayOrderSummary() {
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Total Amount: $" + totalAmount);
        System.out.println("Date/Time: " + datetime);
        System.out.println("Status: " + status);
        System.out.println("Cart Details:");
        cart.viewCart();
    }
    
    // Database operations - write order to text file
    public String toFileString() {
        return orderId + "|" + customerId + "|" + totalAmount + "|" + 
               datetime.toString() + "|" + status.name();
    }
    
    // Read order from text file line
    public static Order fromFileString(String fileLine, ShoppingCart cart) {
        String[] parts = fileLine.split("\\|");
        Order order = new Order(parts[0], parts[1], cart);
        order.totalAmount = Double.parseDouble(parts[2]);
        order.datetime = LocalDateTime.parse(parts[3]);
        order.status = OrderStatus.valueOf(parts[4]);
        return order;
    }

    
    // Alternative method for programmatic status change
    public void changeStatus(OrderStatus newStatus) {
        this.status = newStatus;
        System.out.println("Status changed to: " + newStatus);
    }
    
    public void displayOrderSummary() {
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Total Amount: $" + totalAmount);
        System.out.println("Date/Time: " + datetime);
        System.out.println("Status: " + status);
        System.out.println("Cart Details:");
        cart.viewCart();
    }
    
    // Database operations - write order to text file
    public String toFileString() {
        return orderId + "|" + customerId + "|" + totalAmount + "|" + 
               datetime.toString() + "|" + status.name();
    }
    
    // Read order from text file line
    public static Order fromFileString(String fileLine, ShoppingCart cart) {
        String[] parts = fileLine.split("\\|");
        Order order = new Order(parts[0], parts[1], cart);
        order.totalAmount = Double.parseDouble(parts[2]);
        order.datetime = LocalDateTime.parse(parts[3]);
        order.status = OrderStatus.valueOf(parts[4]);
        return order;
    }
}