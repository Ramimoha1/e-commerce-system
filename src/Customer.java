public class Customer extends User {
    
    private ShoppingCart cart;

    public Customer(String name, String username, String password, String address, ShoppingCart cart) {
        super(name, username, password, address);
        this.cart = cart;
    }

    public void viewItems() {

    }

    public void addToCart() {

    }

    public void updateCartItem() {

    }

    public void removeFromCart() {

    }

    public void updateAddress(String newAddress) {
        setAddress(newAddress);
    }
}
