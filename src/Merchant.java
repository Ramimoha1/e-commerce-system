public class Merchant extends User {
    private static int countMerchant;
    private Store store;
    private String merchantId;

    public Merchant(String name, String username, String password, String address, String storeName) {
        super(name, username, password, address); // initialize User part
        merchantId = String.format("%04d", ++countMerchant); // e.g., 0001, 0002
        store = new Store(storeName, getName(), merchantId); // getName() from User
    }

    public Store getStore() {
        return store;
    }

    public String getMerchantId() {
        return merchantId;
    }
}
