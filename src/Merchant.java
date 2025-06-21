import java.io.*;
import java.util.*;

public class Merchant extends User {    // Inheritance
    private String storeName;
    private List<Product> productList;  // Composition

    public Merchant(String username, String password, String name, String address) {
        super(username, password, name, address);   // to initialize inherited fields from User
        this.productList = new ArrayList<>();   
        this.storeName = "Default Store";
    }

    // Polymorphism
    @Override
    public void viewDashboard() {
        System.out.println("Welcome, Merchant " + name);
        viewInventory();
    }

    public boolean addProduct(Product product) {
        if (product == null || product.getProdID() == null || product.getProdID().trim().isEmpty()) {
            System.out.println("Cannot add product: invalid product.");
            return false;
        }

        String newProdID = product.getProdID().trim();

        File file = new File("product.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";", -1);
                    if (parts.length >= 1) {
                        String fileProdID = parts[0].trim();
                        if (fileProdID.equalsIgnoreCase(newProdID)) {
                            System.out.println("Cannot add product: duplicate product ID.");
                            return false;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading product.txt: " + e.getMessage());
                return false;
            }
        }

        productList.add(product);
        System.out.println("Product added successfully.");
        return true;
    }

    public void deleteProduct(String productId) {
        File inputFile = new File("product.txt");
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length > 0 && parts[0].trim().equalsIgnoreCase(productId)) {
                    found = true; // Skip this line (don't add it to updatedLines)
                    continue;
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("Product ID not found.");
            return;
        }

        // Rewrite the file with remaining lines
        try (PrintWriter writer = new PrintWriter(new FileWriter(inputFile))) {
            for (String updatedLine : updatedLines) {
                writer.println(updatedLine);
            }
            System.out.println("Product with ID " + productId + " deleted successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void editProduct(String productId, String newName, String newDescription, double newPrice, int newStock) {
        if (productId == null || productId.trim().isEmpty()) {
            System.out.println("Invalid product ID.");
            return;
        }

        for (Product p : productList) {
            if (p.getProdID().equals(productId)) {
                if (newName != null && !newName.trim().isEmpty()) {
                    p.setProdName(newName);
                }
                if (newDescription != null && !newDescription.trim().isEmpty()) {
                    p.setProdDesc(newDescription);
                }

                p.updatePrice(newPrice);
                p.updateStock(newStock);

                System.out.println("Product updated successfully.");
                return;
            }
        }

        System.out.println("Product with ID " + productId + " not found.");
    }

    public void viewInventory() {
        for (Product p : productList) {
            System.out.println(p);
        }
    }

    public void updateStoreInfo(String newStoreName) {
        this.storeName = newStoreName;
    }
}

