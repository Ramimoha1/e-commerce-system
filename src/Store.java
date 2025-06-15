import java.util.ArrayList;
import java.util.Scanner ;

public class Store  {
    private String storeName;
    private String ownerName;
    private String ownerId;
    private ArrayList<Product> productList;
    
    public Store( String storeName , String ownerName , String ownerId)
    {
        this.storeName = storeName;
        this.ownerName = ownerName;
        this.ownerId = ownerId;
        productList = new ArrayList<>();
    }
    public void addProduct(Product product)
    {
        productList.add(product);
    }
    public void deleteProduct(Product productID)
    {
        productList.remove(getProductById(productID.getId())); 
    }
   public Product getProductById(String id) { 
    for (int i = 0; i < productList.size(); i++) { // search elements sequenctially 
        if (productList.get(i).getId().equals(id)) {
            return productList.get(i);
        }
    }
    return null; // Product not found
    }

public void editProduct(Product product) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("=== Edit Product ===");

    // update Id
    System.out.println("Old Product ID: " + product.getId());
    System.out.print("Enter new Product ID: ");
    String newId = scanner.nextLine();
    product.setId(newId);

    scanner.nextLine();

    // update name
    System.out.println("Old Product Name: " + product.getProdName());
    System.out.print("Enter new Product Name: ");
    String newName = scanner.nextLine();
    product.setProdName(newName);

    // update price
    System.out.println("Old Product Price: " + product.getProdPrice());
    System.out.print("Enter new Product Price: ");
    double newPrice = scanner.nextDouble();
    product.setProdPrice(newPrice);

    // update  stock
    System.out.println("Old Stock Quantity: " + product.getStockQuantity());
    System.out.print("Enter new Stock Quantity: ");
    int newStock = scanner.nextInt();
    product.setStockQuantity(newStock);

    System.out.println("Product updated successfully!");
}
public void displayStoreInfo() {
    System.out.println("=== Store Product List ===");

    // Header
    System.out.printf("%-10s %-20s %-10s %-15s%n", "Product ID", "Product Name", "Price", "Stock Quantity");
    System.out.println("---------------------------------------------------------------------");

    // Iterate and display each product
    for (Product product : productList) {
        System.out.printf(
            "%-10d %-20s %-10.2f %-15d%n",
            product.getId(),
            product.getProdName(),
            product.getProdPrice(),
            product.getStockQuantity()
        );
    }

    System.out.println("---------------------------------------------------------------------");
}

    
}