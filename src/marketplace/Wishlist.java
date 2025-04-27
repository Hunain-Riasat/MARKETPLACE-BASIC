package marketplace;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Wishlist implements Serializable {
    private String userId;
    private List<Product> products;
    
    public Wishlist(String userId) {
        this.userId = userId;
        this.products = new ArrayList<>();
    }
    
    public String getUserId() { return userId; }
    
    public List<Product> getProducts() { return products; }
    
    public void addProduct(Product product) {
        if (!products.contains(product)) {
            products.add(product);
            saveToFile();
            System.out.println(product.getName() + " added to wishlist");
        } else {
            System.out.println(product.getName() + " is already in wishlist");
        }
    }
    
    public void removeProduct(Product product) {
        products.remove(product);
        saveToFile();
        System.out.println(product.getName() + " removed from wishlist");
    }
    
    public boolean containsProduct(Product product) {
        return products.contains(product);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Wishlist:\n");
        if (products.isEmpty()) {
            sb.append("  (Your wishlist is empty)");
        } else {
            for (Product product : products) {
                sb.append("  ").append(product.getName())
                  .append(" - $").append(product.getPrice())
                  .append("\n");
            }
        }
        return sb.toString();
    }
    
    // File handling methods
    public void saveToFile() {
        try {
            File dir = new File("wishlists");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fileOut = new FileOutputStream("wishlists/" + userId + ".dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Error saving wishlist data: " + e.getMessage());
        }
    }
    
    public static Wishlist loadFromFile(String userId) {
        Wishlist wishlist = null;
        try {
            FileInputStream fileIn = new FileInputStream("wishlists/" + userId + ".dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            wishlist = (Wishlist) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            // If wishlist doesn't exist, create a new one
            wishlist = new Wishlist(userId);
        }
        return wishlist;
    }
}
