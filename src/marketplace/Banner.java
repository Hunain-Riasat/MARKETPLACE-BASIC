package marketplace;
import java.io.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;


public class Banner implements Serializable {
    private String bannerId;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
    
    public Banner(String title, String content, LocalDateTime startDate, LocalDateTime endDate) {
        this.bannerId = "BNR-" + UUID.randomUUID().toString().substring(0, 8);
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = true;
    }
    
    public String getBannerId() { return bannerId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    
    public boolean isActive() { 
        LocalDateTime now = LocalDateTime.now();
        return active && now.isAfter(startDate) && now.isBefore(endDate);
    }
    
    public void setActive(boolean active) { this.active = active; }
    
    @Override
    public String toString() {
        String status = isActive() ? "ACTIVE" : "INACTIVE";
        return "Banner: " + title + " [" + status + "]\n" + content;
    }
    
    // File handling methods
    public void saveToFile() {
        try {
            File dir = new File("data/banners");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fileOut = new FileOutputStream("data/banners/" + bannerId + ".dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Error saving banner data: " + e.getMessage());
        }
    }
    
    public static Banner loadFromFile(String bannerId) {
        Banner banner = null;
        try {
            FileInputStream fileIn = new FileInputStream("data/banners/" + bannerId + ".dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            banner = (Banner) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading banner data: " + e.getMessage());
        }
        return banner;
    }
    
    public static List<Banner> loadActiveBanners() {
        List<Banner> activeBanners = new ArrayList<>();
        File dir = new File("data/banners");
        if (dir.exists()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".dat"));
            if (files != null) {
                for (File file : files) {
                    String bannerId = file.getName().replace(".dat", "");
                    Banner banner = loadFromFile(bannerId);
                    if (banner != null && banner.isActive()) {
                        activeBanners.add(banner);
                    }
                }
            }
        }
        return activeBanners;
    }
}
