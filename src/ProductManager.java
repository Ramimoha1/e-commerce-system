// Serves as a controller class to manage a collection of Product objects
import java.io.*;
import java.util.*;

public class ProductManager {
    private List<Product> products = new ArrayList<>();

    public void loadFromFile(String filename) {
        // Uses a BufferedReader to read each line from the file.
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                Product p = Product.fromFileString(line);
                if (p != null) {
                    products.add(p);
                } else {
                    System.err.println("Skipped invalid product line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + filename);
            System.err.println("Details: " + e.getMessage());
        }
    }

    public void saveToFile(String filename) {
        // Uses a PrintWriter to write product data back to the file using toFileString() method.
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Product p : products) {
                pw.println(p.toFileString());
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filename);
            System.err.println("Details: " + e.getMessage());
        }
    }

    public Product getProductById(String productId) {
        for (Product p : products) {
            if (p.getProdID().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    public void addProduct(Product p) {
        if (getProductById(p.getProdID()) != null) {
            System.out.println("Product with ID " + p.getProdID() + " already exists.");
            return;
        }
        products.add(p);
    }

    public List<Product> getProducts() {
        return products;
    }
}


