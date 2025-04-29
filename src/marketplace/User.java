package marketplace;
import java.io.*;
import java.util.ArrayList;

public class User implements Serializable {
    private String userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    
    public User(String userId, String username, String password, String email, String phone, String address) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
    
    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    
    public void updateProfile(String email, String phone, String address) {
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
    
    @Override
    public String toString() {
        return "User ID: " + userId + "\nUsername: " + username + "\nEmail: " + email;
    }
    
    // File handling methods
    public void saveToFile() {
        try {
            File dir = new File("data/users");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fileOut = new FileOutputStream("data/users/" + userId + ".dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("User data saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }
    
    public static User loadFromFile(String userId) {
        User user = null;
        try {
            FileInputStream fileIn = new FileInputStream("data/users/" + userId + ".dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            user = (User) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
        return user;
    }
}
