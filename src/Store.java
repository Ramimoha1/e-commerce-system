import java.util.ArrayList;
import java.util.List;

public class Store implements InventoryManager {    // implements InventoryManager interface
    private String merchantId;
    private List<Product> productList;

    // Constructor
    public Store(String merchantId) {
        this.merchantId = merchantId;
        this.productList = new ArrayList<>();
    }

    // Add a product to the store
    @Override
    public void addProduct(Product product) {
        for (Product p : productList) {
            if (p.getProdID().equals(product.getProdID())) {
                System.out.println("Product with ID " + product.getProdID() + " already exists.");
                return;
            }
        }
        productList.add(product);
    }

    // Get a product by its ID
    @Override
    public Product getProductById(String productId) {
        for (Product product : productList) {
            if (product.getProdID().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    // Remove a product by its ID
    @Override
    public void removeProduct(String productId) {
        productList.removeIf(product -> product.getProdID().equals(productId));
    }

    // Display inventory
    @Override
    public void displayInventory() {
        System.out.println("Store Inventory for Merchant ID: " + merchantId);
        for (Product product : productList) {
            System.out.println(product);
        }
    }

    public String getMerchantId() {
        return merchantId;
    }

    public List<Product> getProductList() {
        return productList;
    }
}



