 class Merchant extends User {
    private Store store;
    
    public Merchant(String storeName)
    {

        store = new Store(super.name , storeName)
      
    }
    
    
}
class Product
{

}
 class Store  {
    private String storeName;
    private String ownerName;
    private ArrayList<Product> productList;
    
    public Store(ownerName, storeName)
    {
        this.storeName = storeName;
        this.ownerName = ownerName;
        productList = new ArrayList<>;
    }
    public void addProduct(String product)
    {
        productList.add(product);
    }
    public void deleteProduct(String productID)
    {
        productList.remove(getProductByid())
    }
    public int getProductByid(String pr)
    public void editProduct(p)
    
}
 class Order extends User {
    private String storeName;
    private ArrayList<Product> productList;
    
    public addProduct(String product)
    {
        productList.add(product);
    }
    public deleteProduct(String productID)
    {
        productList.remove(getProductByid())
    }
    
}
class Product
{

}