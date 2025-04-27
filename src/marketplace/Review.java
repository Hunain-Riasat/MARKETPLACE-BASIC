package marketplace;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Review implements Serializable {
    private String reviewId;
    private String userId;
    private String productId;
    private int rating; // 1-5 stars
    private String comment;
    private LocalDateTime date;
    
    public Review(String userId, String productId, int rating, String comment) {
        this.reviewId = "REV-" + UUID.randomUUID().toString().substring(0, 8);
        this.userId = userId;
        this.productId = productId;
        this.rating = (rating < 1) ? 1 : (rating > 5) ? 5 : rating; // Ensure 1-5 range
        this.comment = comment;
        this.date = LocalDateTime.now();
    }
    
    public String getReviewId() { return reviewId; }
    
    public String getUserId() { return userId; }
    
    public String getProductId() { return productId; }
    
    public int getRating() { return rating; }
    
    public String getComment() { return comment; }
    
    public LocalDateTime getDate() { return date; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < rating; i++) {
            stars.append("★");
        }
        for (int i = rating; i < 5; i++) {
            stars.append("☆");
        }
        
        return stars.toString() + " " + comment + " (Posted on " + date.format(formatter) + ")";
    }
    
    // File handling methods
    public void saveToFile() {
        try {
            File dir = new File("reviews");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fileOut = new FileOutputStream("reviews/" + reviewId + ".dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Error saving review data: " + e.getMessage());
        }
    }
    
    public static Review loadFromFile(String reviewId) {
        Review review = null;
        try {
            FileInputStream fileIn = new FileInputStream("reviews/" + reviewId + ".dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            review = (Review) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading review data: " + e.getMessage());
        }
        return review;
    }
}
