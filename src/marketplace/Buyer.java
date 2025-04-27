package marketplace;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Buyer extends User {
    private Cart cart;
    private List<Order> orderHistory;
    private Wishlist wishlist;
    
    public Buyer(String userId, String username, String password, String email, String phone, String address) {
        super(userId, username, password, email, phone, address);
        this.cart = new Cart(userId);
        this.orderHistory = new ArrayList<>();
        this.wishlist = new Wishlist(userId);
    }
    
    public Cart getCart() { return cart; }
    
    public List<Order> getOrderHistory() { return orderHistory; }
    
    public Wishlist getWishlist() { return wishlist; }
    
    public void addToCart(Product product, int quantity) {
        cart.addItem(product, quantity);
        System.out.println(product.getName() + " added to cart. Quantity: " + quantity);
    }
    
    public void removeFromCart(Product product) {
        cart.removeItem(product);
        System.out.println(product.getName() + " removed from cart.");
    }
    
    public void addToWishlist(Product product) {
        wishlist.addProduct(product);
        System.out.println(product.getName() + " added to wishlist.");
    }
    
    public void removeFromWishlist(Product product) {
        wishlist.removeProduct(product);
        System.out.println(product.getName() + " removed from wishlist.");
    }
    
    public Order placeOrder(Payment payment) {
        if (cart.getItems().isEmpty()) {
            System.out.println("Cannot place order with empty cart.");
            return null;
        }
        
        Order order = new Order(this, cart.getItems(), payment);
        orderHistory.add(order);
        cart.clear();
        order.saveToFile();
        System.out.println("Order placed successfully! Order ID: " + order.getOrderId());
        return order;
    }
    
    public void addReview(Product product, int rating, String comment) {
        Review review = new Review(this.getUserId(), product.getProductId(), rating, comment);
        product.addReview(review);
        review.saveToFile();
        System.out.println("Review added for " + product.getName());
    }
    
    // File handling methods with inheritance
    @Override
    public void saveToFile() {
        try {
            File dir = new File("buyers");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fileOut = new FileOutputStream("buyers/" + getUserId() + ".dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Buyer data saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving buyer data: " + e.getMessage());
        }
    }
    
    public static Buyer loadFromFile(String userId) {
        Buyer buyer = null;
        try {
            FileInputStream fileIn = new FileInputStream("buyers/" + userId + ".dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            buyer = (Buyer) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading buyer data: " + e.getMessage());
        }
        return buyer;
    }
}
