public interface InventoryManager {
    void addProduct(Product product);
    Product getProductById(String productId);
    void removeProduct(String productId);
    void displayInventory();
}
