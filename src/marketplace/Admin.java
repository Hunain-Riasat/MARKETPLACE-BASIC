package marketplace;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Admin extends User {
    private List<Banner> banners;
    
    public Admin(String userId, String username, String password, String email, String phone, String address) {
        super(userId, username, password, email, phone, address);
        this.banners = new ArrayList<>();
    }
    
    public void addBanner(Banner banner) {
        banners.add(banner);
        banner.saveToFile();
        System.out.println("Banner added: " + banner.getTitle());
    }
    
    public void removeBanner(Banner banner) {
        banners.remove(banner);
        File file = new File("data/banners/" + banner.getBannerId() + ".dat");
        if (file.exists()) {
            file.delete();
        }
        System.out.println("Banner removed: " + banner.getTitle());
    }
    
    public List<Banner> getBanners() {
        return banners;
    }
    
    public void addCategory(Category category) {
        category.saveToFile();
        System.out.println("Category added: " + category.getName());
    }
    
    public void removeCategory(Category category) {
        File file = new File("data/categories/" + category.getCategoryId() + ".dat");
        if (file.exists()) {
            file.delete();
        }
        System.out.println("Category removed: " + category.getName());
    }
    
    public void blockUser(User user) {
        // Implementing a simple blocking mechanism
        user.setPassword("BLOCKED");
        user.saveToFile();
        System.out.println("User blocked: " + user.getUsername());
    }
    
    // File handling methods
    @Override
    public void saveToFile() {
        try {
            File dir = new File("data/admins");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fileOut = new FileOutputStream("data/admins/" + getUserId() + ".dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Admin data saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving admin data: " + e.getMessage());
        }
    }
    
    public static Admin loadFromFile(String userId) {
        Admin admin = null;
        try {
            FileInputStream fileIn = new FileInputStream("data/admins/" + userId + ".dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            admin = (Admin) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading admin data: " + e.getMessage());
        }
        return admin;
    }
}
