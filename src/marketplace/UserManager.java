package marketplace;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManager {
    // Updated: Use data/ subfolders for all directories
    private static final String BUYERS_DIR = "data/buyers";
    private static final String SELLERS_DIR = "data/sellers";
    private static final String ADMINS_DIR = "data/admins";
    
    private static UserManager instance;
    private User currentUser;
    
    private UserManager() {
        // Create necessary directories in data/
        createDirectory(BUYERS_DIR);
        createDirectory(SELLERS_DIR);
        createDirectory(ADMINS_DIR);
    }
    
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    
    private void createDirectory(String dirName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public Buyer registerBuyer(String username, String password, String email, String phone, String address) {
        if (usernameExists(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return null;
        }
        
        String userId = "B-" + UUID.randomUUID().toString().substring(0, 8);
        Buyer buyer = new Buyer(userId, username, password, email, phone, address);
        buyer.saveToFile();
        System.out.println("Buyer registered successfully with ID: " + userId);
        return buyer;
    }
    
    public Seller registerSeller(String username, String password, String email, String phone, String address) {
        if (usernameExists(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return null;
        }
        
        String userId = "S-" + UUID.randomUUID().toString().substring(0, 8);
        Seller seller = new Seller(userId, username, password, email, phone, address);
        seller.saveToFile();
        System.out.println("Seller registered successfully with ID: " + userId);
        return seller;
    }
    
    public Admin registerAdmin(String username, String password, String email, String phone, String address) {
        if (usernameExists(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return null;
        }
        
        String userId = "A-" + UUID.randomUUID().toString().substring(0, 8);
        Admin admin = new Admin(userId, username, password, email, phone, address);
        admin.saveToFile();
        System.out.println("Admin registered successfully with ID: " + userId);
        return admin;
    }
    
    public User login(String username, String password) {
        // Try to find the user in all directories
        User user = findUserByUsername(username);
        
        if (user != null && user.login(username, password)) {
            currentUser = user;
            System.out.println("Login successful. Welcome, " + username + "!");
            return user;
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }
    
    public void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }
    
    private boolean usernameExists(String username) {
        return findUserByUsername(username) != null;
    }
    
    private User findUserByUsername(String username) {
        // Check buyers
        User user = findUserInDirectory(BUYERS_DIR, username);
        if (user != null) return user;
        
        // Check sellers
        user = findUserInDirectory(SELLERS_DIR, username);
        if (user != null) return user;
        
        // Check admins
        user = findUserInDirectory(ADMINS_DIR, username);
        return user;
    }
    
    private User findUserInDirectory(String dirName, String username) {
        File dir = new File(dirName);
        if (!dir.exists()) return null;
        
        File[] files = dir.listFiles((d, name) -> name.endsWith(".dat"));
        if (files == null) return null;
        
        for (File file : files) {
            try {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                User user = (User) in.readObject();
                in.close();
                fileIn.close();
                
                if (user.getUsername().equals(username)) {
                    return user;
                }
            } catch (Exception e) {
                // Skip files that can't be read properly
            }
        }
        
        return null;
    }
    
    public List<Buyer> getAllBuyers() {
        return loadUsersFromDirectory(BUYERS_DIR, Buyer.class);
    }
    
    public List<Seller> getAllSellers() {
        return loadUsersFromDirectory(SELLERS_DIR, Seller.class);
    }
    
    public List<Admin> getAllAdmins() {
        return loadUsersFromDirectory(ADMINS_DIR, Admin.class);
    }
    
    @SuppressWarnings("unchecked")
    private <T extends User> List<T> loadUsersFromDirectory(String dirName, Class<T> clazz) {
        List<T> users = new ArrayList<>();
        File dir = new File(dirName);
        if (!dir.exists()) return users;
        
        File[] files = dir.listFiles((d, name) -> name.endsWith(".dat"));
        if (files == null) return users;
        
        for (File file : files) {
            try {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                Object obj = in.readObject();
                in.close();
                fileIn.close();
                
                if (clazz.isInstance(obj)) {
                    users.add((T) obj);
                }
            } catch (Exception e) {
                // Skip files that can't be read properly
            }
        }
        
        return users;
    }
}
