package marketplace;
import java.io.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class Payment implements Serializable {
    private String paymentId;
    private double amount;
    private String paymentMethod; // "Credit Card", "PayPal", "Cash on Delivery"
    private LocalDateTime paymentDate;
    private String status; // "Pending", "Completed", "Failed", "Refunded"
    private String transactionDetails;
    
    public Payment(double amount, String paymentMethod, String transactionDetails) {
        this.paymentId = "PMT-" + UUID.randomUUID().toString().substring(0, 8);
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDateTime.now();
        this.status = "Pending";
        this.transactionDetails = transactionDetails;
    }
    
    public String getPaymentId() { return paymentId; }
    
    public double getAmount() { return amount; }
    
    public String getPaymentMethod() { return paymentMethod; }
    
    public LocalDateTime getPaymentDate() { return paymentDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status; 
        saveToFile();
        System.out.println("Payment status updated to: " + status);
    }
    
    public String getTransactionDetails() { return transactionDetails; }
    
    public boolean processPayment() {
        // Simulate payment processing
        boolean success = Math.random() > 0.1; // 90% success rate
        
        if (success) {
            this.status = "Completed";
            System.out.println("Payment processed successfully");
        } else {
            this.status = "Failed";
            System.out.println("Payment processing failed");
        }
        
        saveToFile();
        return success;
    }
    
    @Override
    public String toString() {
        return "Payment ID: " + paymentId + "\nAmount: $" + amount +
               "\nMethod: " + paymentMethod + "\nStatus: " + status;
    }
    
    // File handling methods
    public void saveToFile() {
        try {
            File dir = new File("data/payments");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fileOut = new FileOutputStream("data/payments/" + paymentId + ".dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Error saving payment data: " + e.getMessage());
        }
    }
    
    public static Payment loadFromFile(String paymentId) {
        Payment payment = null;
        try {
            FileInputStream fileIn = new FileInputStream("data/payments/" + paymentId + ".dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            payment = (Payment) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading payment data: " + e.getMessage());
        }
        return payment;
    }
}
