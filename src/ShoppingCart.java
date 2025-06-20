public class ShoppingCart {
    private CartItem[] items;   //Composition
    private int itemCount;

    private static final int MAX_ITEMS = 100;

    public ShoppingCart() {
        items = new CartItem[MAX_ITEMS];
        itemCount = 0;
    }

    public void addProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            System.out.println("Invalid product or quantity.");
            return;
        }

        for (int i = 0; i < itemCount; i++) {
            if (items[i].getProduct().getProdID().equals(product.getProdID())) {
                items[i].setItemQuantity(items[i].getItemQuantity() + quantity);
                System.out.println("Product already in cart. Quantity updated.");
                return;
            }
        }

        if (itemCount < MAX_ITEMS) {
            items[itemCount++] = new CartItem(product, quantity);
        } else {
            System.out.println("Cart is full. Cannot add more items.");
        }
    }

    public void updateProduct(Product product, int newQuantity) {
        if (product == null || newQuantity < 0) {
            System.out.println("Invalid product or quantity.");
            return;
        }

        for (int i = 0; i < itemCount; i++) {
            if (items[i].getProduct().getProdID().equals(product.getProdID())) {
                items[i].setItemQuantity(newQuantity);
                return;
            }
        }

        System.out.println("Product not found in cart.");
    }

    public void removeProduct(Product product) {
        if (product == null) {
            System.out.println("Invalid product.");
            return;
        }

        for (int i = 0; i < itemCount; i++) {
            if (items[i].getProduct().getProdID().equals(product.getProdID())) {
                // Shift items to the left
                for (int j = i; j < itemCount - 1; j++) {
                    items[j] = items[j + 1];
                }
                items[--itemCount] = null;
                return;
            }
        }

        System.out.println("Product not found in cart.");
    }

    public void viewCart() {
        if (itemCount == 0) {
            System.out.println("Cart is empty.");
        } else {
            for (int i = 0; i < itemCount; i++) {
                System.out.println(items[i]);
            }
        }
    }

    public double calculateTotal() {
        double total = 0;
        for (int i = 0; i < itemCount; i++) {
            total += items[i].getProduct().getProdPrice() * items[i].getItemQuantity();
        }
        return total;
    }

    public void clear() {
        for (int i = 0; i < itemCount; i++) {
            items[i] = null;
        }
        itemCount = 0;
    }

    public boolean contains(Product product) {
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getProduct().getProdID().equals(product.getProdID())) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return itemCount == 0;
    }

    public int getQuantityForProduct(Product product) {
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getProduct().getProdID().equals(product.getProdID())) {
                return items[i].getItemQuantity();
            }
        }
        return 0;
    }

    public CartItem[] getItems() {
        CartItem[] currentItems = new CartItem[itemCount];
        for (int i = 0; i < itemCount; i++) {
            currentItems[i] = items[i];
        }
        return currentItems;
    }
}