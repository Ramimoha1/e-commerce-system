import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private String orderId;
    private String customerId;
    private ShoppingCart cart;  // Composition
    private double totalAmount;
    private LocalDateTime datetime;
    private String status;

    // Constructor
    public Order(String orderId, String customerId, ShoppingCart cart) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty.");
        }
        if (cart == null) {
            throw new IllegalArgumentException("ShoppingCart cannot be null.");
        }

        this.orderId = orderId;
        this.customerId = customerId;
        this.cart = cart;
        this.totalAmount = cart.calculateTotal();
        this.datetime = LocalDateTime.now();
        this.status = "Pending";
    }


    // Change order status
    public void changeStatus(String newStatus) {
        List<String> allowedStatuses = List.of("Pending", "Paid", "Cancelled");

        if (allowedStatuses.contains(newStatus)) {
            this.status = newStatus;
        } else {
            System.out.println("Invalid status: " + newStatus);
        }
    }

    // Display order summary
    public void displayOrderSummary() {
        System.out.println("===== Order Summary =====");
        System.out.println("Order ID   : " + orderId);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Date       : " + datetime);
        System.out.println("Status     : " + status);
        System.out.println("--- Cart Items ---");
        cart.viewCart();  // This prints items from ShoppingCart
        System.out.printf("Total      : RM %.2f%n", totalAmount);
    }

    // Convert to string for saving to file
    public String toFileString() {
        return orderId + "," + customerId + "," + totalAmount + "," + datetime + "," + status;
    }

    // Create Order from file string – not implemented yet
    public static Order fromFileString(String fileLine) {
        try {
            String[] parts = fileLine.split(",", 5);
            String orderId = parts[0];
            String customerId = parts[1];
            double totalAmount = Double.parseDouble(parts[2]);
            LocalDateTime datetime = LocalDateTime.parse(parts[3]);
            String status = parts[4];

            Order order = new Order(orderId, customerId, new ShoppingCart()); 
            order.totalAmount = totalAmount;
            order.datetime = datetime;
            order.status = status;

            return order;
        } catch (Exception e) {
            System.out.println("Error reading order from file: " + e.getMessage());
            return null;
        }
    }

    // Getters 
    public String getOrderId() {
        return orderId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public ShoppingCart getCart() {
        return cart;
    }
}
