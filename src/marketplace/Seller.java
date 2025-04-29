package marketplace;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Seller extends User {
    private List<Product> products;
    private List<Order> receivedOrders;
    private double rating;
    private int totalRatings;
    
    public Seller(String userId, String username, String password, String email, String phone, String address) {
        super(userId, username, password, email, phone, address);
        this.products = new ArrayList<>();
        this.receivedOrders = new ArrayList<>();
        this.rating = 0.0;
        this.totalRatings = 0;
    }
    
    public List<Product> getProducts() { return products; }
    
    public List<Order> getReceivedOrders() { return receivedOrders; }
    
    public double getRating() { return rating; }
    
    public Product addProduct(String productId, String name, String description, double price, int stock, Category category) {
        Product product = new Product(productId, name, description, price, stock, category, this.getUserId());
        products.add(product);
        product.saveToFile();
        System.out.println("Product added: " + name);
        return product;
    }
    
    public void removeProduct(Product product) {
        products.remove(product);
        // Remove product file
        File file = new File("data/products/" + product.getProductId() + ".dat");
        if (file.exists()) {
            file.delete();
        }
        System.out.println("Product removed: " + product.getName());
    }
    
    public void updateProduct(Product product, String name, String description, double price, int stock, Category category) {
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        product.saveToFile();
        System.out.println("Product updated: " + name);
    }
    
    public void receiveOrder(Order order) {
        receivedOrders.add(order);
        System.out.println("New order received! Order ID: " + order.getOrderId());
    }
    
    public void updateRating(double newRating) {
        double totalRating = (this.rating * this.totalRatings) + newRating;
        this.totalRatings++;
        this.rating = totalRating / this.totalRatings;
        System.out.println("Seller rating updated to: " + this.rating);
    }
    
    // File handling methods with inheritance
    @Override
    public void saveToFile() {
        try {
            File dir = new File("data/sellers");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fileOut = new FileOutputStream("data/sellers/" + getUserId() + ".dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Seller data saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving seller data: " + e.getMessage());
        }
    }
    
    public static Seller loadFromFile(String userId) {
        Seller seller = null;
        try {
            FileInputStream fileIn = new FileInputStream("data/sellers/" + userId + ".dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            seller = (Seller) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading seller data: " + e.getMessage());
        }
        return seller;
    }
}
