import java.io.Serializable;

public class CartItem implements Serializable {
    private Product product;
    private int itemQuantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.itemQuantity = quantity;
    }

    public void updateQuantity(int newQuantity) {
        this.itemQuantity = newQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return itemQuantity;
    }

    public double getTotalPrice() {
        return itemQuantity * product.getPrice();
    }

    public String toString() {
        return product.getName() + " x " + itemQuantity + " = RM " + getTotalPrice();
    }
}
