public class Product {
    private String prodID;
    private String prodName;
    private String prodDesc;
    private double prodPrice;
    private int stockQuantity;

    // Constructor with validation
    public Product(String prodID, String prodName, String prodDesc, double prodPrice, int stockQuantity) {
        if (prodPrice < 0 || stockQuantity < 0) {
            throw new IllegalArgumentException("Price and stock cannot be negative.");
        }
        this.prodID = prodID;
        this.prodName = prodName;
        this.prodDesc = prodDesc;
        this.prodPrice = prodPrice;
        this.stockQuantity = stockQuantity;
    }

    // Getters
    public String getProdID() {
        return prodID;
    }

    public String getProdName() {
        return prodName;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public double getProdPrice() {
        return prodPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    // Setter method with validation
    public void updateStock(int newStockQuantity) {
        if (newStockQuantity < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.stockQuantity = newStockQuantity;
    }

    public void updatePrice(double newPrice) {
        if (newPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.prodPrice = newPrice;
    }

    // Convert object to a line for saving to file
    public String toFileString() {
        return prodID + ";" + prodName + ";" + prodDesc + ";" + prodPrice + ";" + stockQuantity;
    }

    // Create object from a line read from file
    public static Product fromFileString(String fileLine) {
        String[] parts = fileLine.split(",");
        if (parts.length < 5) return null;

        String id = parts[0];
        String name = parts[1];
        String desc = parts[2];
        double price = Double.parseDouble(parts[3]);
        int stock = Integer.parseInt(parts[4]);

        return new Product(id, name, desc, price, stock);
    }

    public void setProdName(String name) { this.prodName = name; }

    public void setProdDesc(String desc) { this.prodDesc = desc; }

    @Override
    public String toString() {
        return "Product ID: " + prodID +
            "\nName: " + prodName +
            "\nDescription: " + prodDesc +
            "\nPrice: RM " + String.format("%.2f", prodPrice) +
            "\nStock: " + stockQuantity + "\n";
    }

    // To properly compare Product objects to check for duplicates in a Set or List
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Product other = (Product) obj;
        return this.prodID != null && this.prodID.equals(other.prodID);
    }

    @Override
    public int hashCode() {
        return prodID != null ? prodID.hashCode() : 0;
    }
}

