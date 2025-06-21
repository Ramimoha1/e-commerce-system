// It extends the User class,  adds specific behavior for managing a shopping cart.
public class Customer extends User {    // Inheritance
    private ShoppingCart cart;  // Composition

    public Customer(String username, String password, String name, String address) {
        super(username, password, name, address);
        this.cart = new ShoppingCart();
    }

    // Polymorphism
    @Override
    public void viewDashboard() {
        System.out.println("Welcome, Customer " + name);
        viewItems();
    }

    // View all items in the cart
    public void viewItems() {
        cart.viewCart();
    }

    // Add a product to the cart
    public void addToCart(Product product, int quantity) {
        cart.addProduct(product, quantity);
    }

    // Update the quantity of a product in the cart
    public void updateCartItem(Product product, int newQuantity) {
        cart.updateProduct(product, newQuantity);
    }

    // Remove a product from the cart
    public void removeFromCart(Product product) {
        cart.removeProduct(product);
    }

    // Change customer's delivery address
    public void updateAddress(String newAddress) {
        this.address = newAddress;
    }

    // Get the shopping cart
    public ShoppingCart getCart() {
        return this.cart;
    }

    public double getCartTotal() {
        return cart.calculateTotal();
    }
}
