import java.io.Serializable;

public class Product {
    private int id;
    private String prodName;
    private String prodDesc;
    private double prodPrice;
    private int stockQuantity;

    public Product(int id, String prodName, String desc, double prodPrice, int stockQuantity) {
        this.id = id;
        this.prodName = prodName;
        this.prodDesc = desc;
        this.prodPrice = prodPrice;
        this.stockQuantity = stockQuantity;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getProdName() {
        return prodName;
    }

    public double getProdPrice() {
        return prodPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setProdPrice(double prodPrice) {
        this.prodPrice = prodPrice;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void updateStock(int newStock){
        this.stockQuantity = new Stock;
    }

     public void updatePrice(double newPrice) {
        this.price = newPrice;
    }

    public String toFileString() {
        return productId + ";" + name + ";" + description + ";" + price + ";" + stockQuantity;
    }

     public static Product fromFileString(String data) {
        String[] parts = data.split(";");
        return new Product(
            parts[0],
            parts[1],
            parts[2],
            Double.parseDouble(parts[3]),
            Integer.parseInt(parts[4])
        );
    }

    public String toString() {
        return "[" + productId + "] " + name + " - " + description + " | RM" + price + " | Stock: " + stockQuantity;
    }
}
