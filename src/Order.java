import java.time.LocalDateTime;

public class Order {
    private String orderId;
    private String customerId;
    private String[] productIds;    // Regular Array
    private int[] quantities;       
    private double[] prices;       
    private double totalAmount;
    private LocalDateTime datetime;
    private String status;
    private int itemCount;

    // Constructor (fixed max items)
    public Order(String orderId, String customerId, int maxItems) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty.");
        }

        this.orderId = orderId;
        this.customerId = customerId;
        this.productIds = new String[maxItems];
        this.quantities = new int[maxItems];
        this.prices = new double[maxItems];
        this.itemCount = 0;
        this.totalAmount = 0;
        this.datetime = LocalDateTime.now();
        this.status = "Pending";
    }

    // Add item to order
    public void addItem(String productId, int quantity, double price) {
        if (itemCount >= productIds.length) {
            System.out.println("Order is full.");
            return;
        }
        productIds[itemCount] = productId;
        quantities[itemCount] = quantity;
        prices[itemCount] = price;
        totalAmount += price * quantity;
        itemCount++;
    }

    // Change order status
    public void changeStatus(String newStatus) {
        String[] allowedStatuses = {"Pending", "Paid", "Cancelled"};
        for (String statusOption : allowedStatuses) {
            if (statusOption.equals(newStatus)) {
                this.status = newStatus;
                return;
            }
        }
        System.out.println("Invalid status: " + newStatus);
    }

    // Display order summary
    public void displayOrderSummary() {
        System.out.println("===== Order Summary =====");
        System.out.println("Order ID   : " + orderId);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Date       : " + datetime);
        System.out.println("Status     : " + status);
        System.out.println("--- Ordered Items ---");
        for (int i = 0; i < itemCount; i++) {
            System.out.printf("Product: %s | Quantity: %d | Price: RM %.2f%n", 
                productIds[i], quantities[i], prices[i]);
        }
        System.out.printf("Total      : RM %.2f%n", totalAmount);
    }

    // Convert to string for saving to file
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(orderId).append(",").append(customerId).append(",")
          .append(totalAmount).append(",").append(datetime).append(",")
          .append(status);

        for (int i = 0; i < itemCount; i++) {
            sb.append(",").append(productIds[i]).append(":")
              .append(quantities[i]).append(":").append(prices[i]);
        }

        return sb.toString();
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

    public int getItemCount() {
        return itemCount;
    }

    public String[] getProductIds() {
        return productIds;
    }

    public int[] getQuantities() {
        return quantities;
    }

    public double[] getPrices() {
        return prices;
    }
}


