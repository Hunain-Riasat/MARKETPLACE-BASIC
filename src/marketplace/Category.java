package marketplace;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
    private String categoryId;
    private String name;
    private String description;
    private List<Product> products;
    
    public Category(String categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.products = new ArrayList<>();
    }
    
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<Product> getProducts() { return products; }
    
    public void addProduct(Product product) {
        products.add(product);
        System.out.println(product.getName() + " added to category " + this.name);
    }
    
    public void removeProduct(Product product) {
        products.remove(product);
        System.out.println(product.getName() + " removed from category " + this.name);
    }
    
    @Override
    public String toString() {
        return "Category: " + name + " (" + description + ")";
    }
    
    // File handling methods
    public void saveToFile() {
        try {
            File dir = new File("categories");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fileOut = new FileOutputStream("categories/" + categoryId + ".dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Category data saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving category data: " + e.getMessage());
        }
    }
    
    public static Category loadFromFile(String categoryId) {
        Category category = null;
        try {
            FileInputStream fileIn = new FileInputStream("categories/" + categoryId + ".dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            category = (Category) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading category data: " + e.getMessage());
        }
        return category;
    }
    
    public static List<Category> loadAllCategories() {
        List<Category> categories = new ArrayList<>();
        File dir = new File("categories");
        if (dir.exists()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".dat"));
            if (files != null) {
                for (File file : files) {
                    String categoryId = file.getName().replace(".dat", "");
                    Category category = loadFromFile(categoryId);
                    if (category != null) {
                        categories.add(category);
                    }
                }
            }
        }
        return categories;
    }
}
