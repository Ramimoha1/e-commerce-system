// CartItem represents one item in the shopping cart, containing:
// A reference to a Product object.
// The quantity of that product selected by the user.

public class CartItem {
    private Product product;    // Composition
    private int itemQuantity; 

    public CartItem(Product product, int itemQuantity) {
        this.product = product;
        this.itemQuantity = itemQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    @Override
    public String toString() {
        return product.getProdID() + ": " + product.getProdName()
            + "\nQuantity: " + itemQuantity
            + "\nPrice: RM " + String.format("%.2f", product.getProdPrice());
    }
}
