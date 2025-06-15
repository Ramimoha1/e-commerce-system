public class Product {
    private int id;
    private String prodName;
    private double prodPrice;
    private int stockQuantity;

    public Product(int id, String prodName, double prodPrice, int stockQuantity) {
        this.id = id;
        this.prodName = prodName;
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
}
