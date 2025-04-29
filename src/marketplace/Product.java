package marketplace;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    private String productId;
    private String name;
    private String description;
    private double price;
    private int stock;
    private Category category;
    private String sellerId;
    private List<Review> reviews;
    private double averageRating;
    
    public Product(String productId, String name, String description, double price, int stock, Category category, String sellerId) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.sellerId = sellerId;
        this.reviews = new ArrayList<>();
        this.averageRating = 0.0;
        if (category != null) {
            category.addProduct(this);
        }
    }
    
    // Getters and setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { 
        if (this.category != null) {
            this.category.removeProduct(this);
        }
        this.category = category;
        if (category != null) {
            category.addProduct(this);
        }
    }
    
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    
    public List<Review> getReviews() { return reviews; }
    
    public double getAverageRating() { return averageRating; }
    
    public void addReview(Review review) {
        reviews.add(review);
        updateAverageRating();
    }
    
    private void updateAverageRating() {
        if (reviews.isEmpty()) {
            averageRating = 0.0;
            return;
        }
        
        double sum = 0.0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        averageRating = sum / reviews.size();
    }
    
    public boolean reduceStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
            saveToFile();
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Product: " + name + "\nPrice: $" + price + 
               "\nStock: " + stock + "\nRating: " + averageRating + 
               "\nDescription: " + description;
    }
    
    // File handling methods
    public void saveToFile() {
        try {
            File dir = new File("data/products");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fileOut = new FileOutputStream("data/products/" + productId + ".dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Product data saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving product data: " + e.getMessage());
        }
    }
    
    public static Product loadFromFile(String productId) {
        Product product = null;
        try {
            FileInputStream fileIn = new FileInputStream("data/products/" + productId + ".dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            product = (Product) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading product data: " + e.getMessage());
        }
        return product;
    }
    
    public static List<Product> loadAllProducts() {
        List<Product> products = new ArrayList<>();
        File dir = new File("products");
        if (dir.exists()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".dat"));
            if (files != null) {
                for (File file : files) {
                    String productId = file.getName().replace(".dat", "");
                    Product product = loadFromFile(productId);
                    if (product != null) {
                        products.add(product);
                    }
                }
            }
        }
        return products;
    }
}
