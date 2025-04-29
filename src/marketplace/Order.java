package marketplace;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;


public class Order implements Serializable {
    private String orderId;
    private String buyerId;
    private Map<Product, Integer> items;
    private double totalAmount;
    private LocalDateTime orderDate;
    private String status; // "Pending", "Shipped", "Delivered", "Cancelled"
    private Payment payment;
    
    public Order(Buyer buyer, Map<Product, Integer> items, Payment payment) {
        this.orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        this.buyerId = buyer.getUserId();
        this.items = new HashMap<>(items); // Make a copy
        this.totalAmount = calculateTotal(items);
        this.orderDate = LocalDateTime.now();
        this.status = "Pending";
        this.payment = payment;
        
        // Update the stock for each product
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            product.reduceStock(quantity);
            
            // Notify seller
            Seller seller = Seller.loadFromFile(product.getSellerId());
            if (seller != null) {
                seller.receiveOrder(this);
                seller.saveToFile();
            }
        }
    }
    
    private double calculateTotal(Map<Product, Integer> items) {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }
    
    public String getOrderId() { return orderId; }
    
    public String getBuyerId() { return buyerId; }
    
    public Map<Product, Integer> getItems() { return items; }
    
    public double getTotalAmount() { return totalAmount; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status; 
        saveToFile();
        System.out.println("Order " + orderId + " status updated to: " + status);
    }
    
    public Payment getPayment() { return payment; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("Date: ").append(orderDate.format(formatter)).append("\n");
        sb.append("Status: ").append(status).append("\n");
        sb.append("Items:\n");
        
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product p = entry.getKey();
            int qty = entry.getValue();
            sb.append("  ").append(qty).append(" x ").append(p.getName())
              .append(" ($").append(p.getPrice()).append(" each)\n");
        }
        
        sb.append("Total Amount: $").append(totalAmount).append("\n");
        sb.append("Payment Method: ").append(payment.getPaymentMethod());
        
        return sb.toString();
    }
    
    // File handling methods
    public void saveToFile() {
        try {
            File dir = new File("data/orders");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fileOut = new FileOutputStream("data/orders/" + orderId + ".dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Error saving order data: " + e.getMessage());
        }
    }
    
    public static Order loadFromFile(String orderId) {
        Order order = null;
        try {
            FileInputStream fileIn = new FileInputStream("data/orders/" + orderId + ".dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            order = (Order) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading order data: " + e.getMessage());
        }
        return order;
    }
}
