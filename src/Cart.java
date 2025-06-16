import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;

public ShoppingCart() {
    items = new ArrayList<>();
}

public void addToCart(CartItem item, int total) {
    System.out.println("Added " + item + " x" + total + " to cart.");
}

public void removeItem(CartItem item) {
    System.out.println("Removed item: " + item);
}

public void updateItemQuantity(CartItem item, int q) {
    System.out.println("Updated quantity of " + item + " to " + q);
}

public void viewCart() {
    System.out.println("Viewing cart contents...");
}

public double calculateTotal() {
    System.out.println("Calculating total...");
    return 0.0;
}

public void clear() {
    System.out.println("Cart cleared.");
}

}
