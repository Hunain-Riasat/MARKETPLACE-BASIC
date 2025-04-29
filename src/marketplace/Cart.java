package marketplace;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Cart implements Serializable {
    private String userId;
    private Map<Product, Integer> items; // Product and quantity
    private double total;
    
    public Cart(String userId) {
        this.userId = userId;
        this.items = new HashMap<>();
        this.total = 0.0;
    }
    
    public String getUserId() { return userId; }
    
    public Map<Product, Integer> getItems() { return items; }
    
    public double getTotal() { return total; }
    
    public void addItem(Product product, int quantity) {
        if (product.getStock() < quantity) {
            System.out.println("Not enough stock available. Only " + product.getStock() + " items left.");
            return;
        }
        
        if (items.containsKey(product)) {
            items.put(product, items.get(product) + quantity);
        } else {
            items.put(product, quantity);
        }
        updateTotal();
        saveToFile();
        System.out.println(quantity + " x " + product.getName() + " added to cart");
    }
    
    public void removeItem(Product product) {
        items.remove(product);
        updateTotal();
        saveToFile();
        System.out.println(product.getName() + " removed from cart");
    }
    
    public void updateQuantity(Product product, int quantity) {
        if (quantity <= 0) {
            removeItem(product);
            return;
        }
        
        if (product.getStock() < quantity) {
            System.out.println("Not enough stock available. Only " + product.getStock() + " items left.");
            return;
        }
        
        items.put(product, quantity);
        updateTotal();
        saveToFile();
        System.out.println(product.getName() + " quantity updated to " + quantity);
    }
    
    public void clear() {
        items.clear();
        total = 0.0;
        saveToFile();
        System.out.println("Cart cleared");
    }
    
    private void updateTotal() {
        total = 0.0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Cart Contents:\n");
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product p = entry.getKey();
            int qty = entry.getValue();
            sb.append(qty).append(" x ").append(p.getName())
              .append(" ($").append(p.getPrice()).append(" each) = $")
              .append(p.getPrice() * qty).append("\n");
        }
        sb.append("Total: $").append(total);
        return sb.toString();
    }
    
    // File handling methods
    public void saveToFile() {
        try {
            File dir = new File("data/carts");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fileOut = new FileOutputStream("data/carts/" + userId + ".dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Error saving cart data: " + e.getMessage());
        }
    }
    
    public static Cart loadFromFile(String userId) {
        Cart cart = null;
        try {
            FileInputStream fileIn = new FileInputStream("data/carts/" + userId + ".dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            cart = (Cart) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            // If cart doesn't exist, create a new one
            cart = new Cart(userId);
        }
        return cart;
    }
}
