import java.util.ArrayList;

public class Store  {
    private String storeName;
    private String ownerName;
    private ArrayList<Product> productList;
    
    public Store( String ownerName, String storeName)
    {
        this.storeName = storeName;
        this.ownerName = ownerName;
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
    public void editProduct(Product product)
    {

    }
    
}