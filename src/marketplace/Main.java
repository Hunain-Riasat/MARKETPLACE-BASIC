package marketplace;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;


public class Main {
    private static UserManager userManager = UserManager.getInstance();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("Welcome to the Marketplace!");
        
        // Create initial demo data if needed
        // createDemoData();
        
        boolean running = true;
        while (running) {
            if (userManager.getCurrentUser() == null) {
                // Not logged in
                System.out.println("\n=== MARKETPLACE ===");
                System.out.println("1. Login");
                System.out.println("2. Register as Buyer");
                System.out.println("3. Register as Seller");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                
                int choice = readIntInput();
                
                switch (choice) {
                    case 1:
                        loginMenu();
                        break;
                    case 2:
                        registerBuyerMenu();
                        break;
                    case 3:
                        registerSellerMenu();
                        break;
                    case 4:
                        running = false;
                        System.out.println("Thank you for using the Marketplace. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                // User is logged in
                User currentUser = userManager.getCurrentUser();
                
                if (currentUser instanceof Buyer) {
                    runBuyerMenu((Buyer) currentUser);
                } else if (currentUser instanceof Seller) {
                    runSellerMenu((Seller) currentUser);
                } else if (currentUser instanceof Admin) {
                    runAdminMenu((Admin) currentUser);
                }
            }
        }
        
        scanner.close();
    }
    
    private static void loginMenu() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        userManager.login(username, password);
    }
    
    private static void registerBuyerMenu() {
        System.out.println("\n=== REGISTER AS BUYER ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        
        Buyer buyer = userManager.registerBuyer(username, password, email, phone, address);
        if (buyer != null) {
            userManager.setCurrentUser(buyer);
        }
    }
    
    private static void registerSellerMenu() {
        System.out.println("\n=== REGISTER AS SELLER ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        
        Seller seller = userManager.registerSeller(username, password, email, phone, address);
        if (seller != null) {
            userManager.setCurrentUser(seller);
        }
    }
    
    private static void runBuyerMenu(Buyer buyer) {
        System.out.println("\n=== BUYER MENU ===");
        System.out.println("1. Browse Products");
        System.out.println("2. View Cart");
        System.out.println("3. View Wishlist");
        System.out.println("4. View Order History");
        System.out.println("5. My Profile");
        System.out.println("6. Logout");
        System.out.print("Enter your choice: ");
        
        int choice = readIntInput();
        
        switch (choice) {
            case 1:
                browseProductsMenu(buyer);
                break;
            case 2:
                viewCartMenu(buyer);
                break;
            case 3:
                viewWishlistMenu(buyer);
                break;
            case 4:
                viewOrderHistoryMenu(buyer);
                break;
            case 5:
                viewProfileMenu(buyer);
                break;
            case 6:
                userManager.logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private static void browseProductsMenu(Buyer buyer) {
        List<Product> products = Product.loadAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        
        System.out.println("\n=== BROWSE PRODUCTS ===");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.println((i + 1) + ". " + p.getName() + " - $" + p.getPrice() + 
                               " (" + p.getAverageRating() + "★)");
        }
        
        System.out.println("0. Back to menu");
        System.out.print("Select a product to view details (1-" + products.size() + "): ");
        int choice = readIntInput();
        
        if (choice > 0 && choice <= products.size()) {
            Product selectedProduct = products.get(choice - 1);
            viewProductDetailsMenu(buyer, selectedProduct);
        }
    }
    
    private static void viewProductDetailsMenu(Buyer buyer, Product product) {
        System.out.println("\n=== PRODUCT DETAILS ===");
        System.out.println(product);
        
        System.out.println("\n=== REVIEWS ===");
        if (product.getReviews().isEmpty()) {
            System.out.println("No reviews yet.");
        } else {
            for (Review review : product.getReviews()) {
                System.out.println(review);
            }
        }
        
        System.out.println("\n1. Add to Cart");
        System.out.println("2. Add to Wishlist");
        System.out.println("3. Write a Review");
        System.out.println("4. Back to Products");
        System.out.print("Enter your choice: ");
        
        int choice = readIntInput();
        
        switch (choice) {
            case 1:
                System.out.print("Enter quantity: ");
                int quantity = readIntInput();
                if (quantity > 0) {
                    buyer.addToCart(product, quantity);
                } else {
                    System.out.println("Invalid quantity.");
                }
                break;
            case 2:
                buyer.addToWishlist(product);
                break;
            case 3:
                System.out.print("Enter rating (1-5): ");
                int rating = readIntInput();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter your review: ");
                String comment = scanner.nextLine();
                buyer.addReview(product, rating, comment);
                break;
            case 4:
                // Do nothing, just go back
                break;
        }
    }
    
    private static void viewCartMenu(Buyer buyer) {
        Cart cart = buyer.getCart();
        if (cart.getItems().isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        
        System.out.println("\n=== YOUR CART ===");
        System.out.println(cart);
        
        System.out.println("\n1. Checkout");
        System.out.println("2. Remove Item");
        System.out.println("3. Update Quantity");
        System.out.println("4. Back to Menu");
        System.out.print("Enter your choice: ");
        
        int choice = readIntInput();
        
        switch (choice) {
            case 1:
                checkoutMenu(buyer);
                break;
            case 2:
                removeCartItemMenu(buyer);
                break;
            case 3:
                updateCartQuantityMenu(buyer);
                break;
            case 4:
                // Do nothing, just go back
                break;
        }
    }
    
    private static void checkoutMenu(Buyer buyer) {
        System.out.println("\n=== CHECKOUT ===");
        System.out.println("Total Amount: $" + buyer.getCart().getTotal());
        
        System.out.println("Select payment method:");
        System.out.println("1. Credit Card");
        System.out.println("2. PayPal");
        System.out.println("3. Cash on Delivery");
        System.out.print("Enter your choice: ");
        
        int choice = readIntInput();
        String paymentMethod;
        String transactionDetails;
        
        switch (choice) {
            case 1:
                paymentMethod = "Credit Card";
                System.out.print("Enter card number: ");
                transactionDetails = scanner.nextLine();
                break;
            case 2:
                paymentMethod = "PayPal";
                System.out.print("Enter PayPal email: ");
                transactionDetails = scanner.nextLine();
                break;
            case 3:
                paymentMethod = "Cash on Delivery";
                transactionDetails = "COD";
                break;
            default:
                System.out.println("Invalid choice, defaulting to Cash on Delivery.");
                paymentMethod = "Cash on Delivery";
                transactionDetails = "COD";
        }
        
        Payment payment = new Payment(buyer.getCart().getTotal(), paymentMethod, transactionDetails);
        if (payment.processPayment()) {
            Order order = buyer.placeOrder(payment);
            if (order != null) {
                System.out.println("Order placed successfully!");
                System.out.println(order);
            }
        } else {
            System.out.println("Payment failed. Please try again.");
        }
    }
    
    private static void removeCartItemMenu(Buyer buyer) {
        Cart cart = buyer.getCart();
        if (cart.getItems().isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        
        int i = 1;
        for (Product product : cart.getItems().keySet()) {
            System.out.println(i + ". " + product.getName());
            i++;
        }
        
        System.out.print("Enter item number to remove: ");
        int choice = readIntInput();
        
        if (choice > 0 && choice <= cart.getItems().size()) {
            Product product = (Product) cart.getItems().keySet().toArray()[choice - 1];
            buyer.removeFromCart(product);
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    private static void updateCartQuantityMenu(Buyer buyer) {
        Cart cart = buyer.getCart();
        if (cart.getItems().isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        
        int i = 1;
        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            System.out.println(i + ". " + entry.getKey().getName() + " (Qty: " + entry.getValue() + ")");
            i++;
        }
        
        System.out.print("Enter item number to update: ");
        int choice = readIntInput();
        
        if (choice > 0 && choice <= cart.getItems().size()) {
            Product product = (Product) cart.getItems().keySet().toArray()[choice - 1];
            System.out.print("Enter new quantity: ");
            int quantity = readIntInput();
            
            if (quantity > 0) {
                cart.updateQuantity(product, quantity);
            } else {
                System.out.println("Invalid quantity.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    private static void viewWishlistMenu(Buyer buyer) {
        Wishlist wishlist = buyer.getWishlist();
        System.out.println("\n=== YOUR WISHLIST ===");
        System.out.println(wishlist);
        
        if (!wishlist.getProducts().isEmpty()) {
            System.out.println("\n1. Add to Cart");
            System.out.println("2. Remove from Wishlist");
            System.out.println("3. Back to Menu");
            System.out.print("Enter your choice: ");
            
            int choice = readIntInput();
            
            switch (choice) {
                case 1:
                    addWishlistItemToCartMenu(buyer);
                    break;
                case 2:
                    removeWishlistItemMenu(buyer);
                    break;
                case 3:
                    // Do nothing, just go back
                    break;
            }
        }
    }
    
    private static void addWishlistItemToCartMenu(Buyer buyer) {
        Wishlist wishlist = buyer.getWishlist();
        if (wishlist.getProducts().isEmpty()) {
            System.out.println("Your wishlist is empty.");
            return;
        }
        
        for (int i = 0; i < wishlist.getProducts().size(); i++) {
            Product p = wishlist.getProducts().get(i);
            System.out.println((i + 1) + ". " + p.getName() + " - $" + p.getPrice());
        }
        
        System.out.print("Enter item number to add to cart: ");
        int choice = readIntInput();
        
        if (choice > 0 && choice <= wishlist.getProducts().size()) {
            Product product = wishlist.getProducts().get(choice - 1);
            System.out.print("Enter quantity: ");
            int quantity = readIntInput();
            
            if (quantity > 0) {
                buyer.addToCart(product, quantity);
            } else {
                System.out.println("Invalid quantity.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    private static void removeWishlistItemMenu(Buyer buyer) {
        Wishlist wishlist = buyer.getWishlist();
        if (wishlist.getProducts().isEmpty()) {
            System.out.println("Your wishlist is empty.");
            return;
        }
        
        for (int i = 0; i < wishlist.getProducts().size(); i++) {
            Product p = wishlist.getProducts().get(i);
            System.out.println((i + 1) + ". " + p.getName() + " - $" + p.getPrice());
        }
        
        System.out.print("Enter item number to remove: ");
        int choice = readIntInput();
        
        if (choice > 0 && choice <= wishlist.getProducts().size()) {
            Product product = wishlist.getProducts().get(choice - 1);
            buyer.removeFromWishlist(product);
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    private static void viewOrderHistoryMenu(Buyer buyer) {
        List<Order> orderHistory = buyer.getOrderHistory();
        if (orderHistory.isEmpty()) {
            System.out.println("You have no order history.");
            return;
        }
        
        System.out.println("\n=== YOUR ORDER HISTORY ===");
        for (int i = 0; i < orderHistory.size(); i++) {
            Order order = orderHistory.get(i);
            System.out.println((i + 1) + ". Order #" + order.getOrderId() + 
                               " - $" + order.getTotalAmount() + 
                               " (" + order.getStatus() + ")");
        }
        
        System.out.print("Enter order number to view details (0 to go back): ");
        int choice = readIntInput();
        
        if (choice > 0 && choice <= orderHistory.size()) {
            Order order = orderHistory.get(choice - 1);
            System.out.println("\n=== ORDER DETAILS ===");
            System.out.println(order);
        }
    }
    
    private static void viewProfileMenu(User user) {
        System.out.println("\n=== PROFILE ===");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Phone: " + user.getPhone());
        System.out.println("Address: " + user.getAddress());
        
        System.out.println("\n1. Update Profile");
        System.out.println("2. Back to Menu");
        System.out.print("Enter your choice: ");
        
        int choice = readIntInput();
        
        if (choice == 1) {
            System.out.print("New Email: ");
            String email = scanner.nextLine();
            System.out.print("New Phone: ");
            String phone = scanner.nextLine();
            System.out.print("New Address: ");
            String address = scanner.nextLine();
            
            user.updateProfile(email, phone, address);
            user.saveToFile();
            System.out.println("Profile updated successfully!");
        }
    }
    
    private static void runSellerMenu(Seller seller) {
        System.out.println("\n=== SELLER MENU ===");
        System.out.println("1. Manage Products");
        System.out.println("2. View Orders");
        System.out.println("3. My Profile");
        System.out.println("4. Logout");
        System.out.print("Enter your choice: ");
        
        int choice = readIntInput();
        
        switch (choice) {
            case 1:
                manageProductsMenu(seller);
                break;
            case 2:
                viewSellerOrdersMenu(seller);
                break;
            case 3:
                viewProfileMenu(seller);
                break;
            case 4:
                userManager.logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private static void manageProductsMenu(Seller seller) {
        System.out.println("\n=== MANAGE PRODUCTS ===");
        System.out.println("1. View My Products");
        System.out.println("2. Add New Product");
        System.out.println("3. Back to Menu");
        System.out.print("Enter your choice: ");
        
        int choice = readIntInput();
        
        switch (choice) {
            case 1:
                viewSellerProductsMenu(seller);
                break;
            case 2:
                addProductMenu(seller);
                break;
            case 3:
                // Do nothing, just go back
                break;
        }
    }
    
    private static void viewSellerProductsMenu(Seller seller) {
        List<Product> products = seller.getProducts();
        if (products.isEmpty()) {
            System.out.println("You haven't added any products yet.");
            return;
        }
        
        System.out.println("\n=== YOUR PRODUCTS ===");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.println((i + 1) + ". " + p.getName() + " - $" + p.getPrice() + 
                               " (Stock: " + p.getStock() + ")");
        }
        
        System.out.println("\n1. Edit Product");
        System.out.println("2. Remove Product");
        System.out.println("3. Back to Menu");
        System.out.print("Enter your choice: ");
        
        int choice = readIntInput();
        
        switch (choice) {
            case 1:
                editProductMenu(seller);
                break;
            case 2:
                removeProductMenu(seller);
                break;
            case 3:
                // Do nothing, just go back
                break;
        }
    }
    
    private static void editProductMenu(Seller seller) {
        List<Product> products = seller.getProducts();
        if (products.isEmpty()) {
            System.out.println("You haven't added any products yet.");
            return;
        }
        
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.println((i + 1) + ". " + p.getName() + " - $" + p.getPrice());
        }
        
        System.out.print("Enter product number to edit: ");
        int choice = readIntInput();
        
        if (choice > 0 && choice <= products.size()) {
            Product product = products.get(choice - 1);
            
            System.out.println("\nEditing Product: " + product.getName());
            System.out.print("New Name (press Enter to keep current): ");
            String name = scanner.nextLine();
            if (name.isEmpty()) name = product.getName();
            
            System.out.print("New Description (press Enter to keep current): ");
            String description = scanner.nextLine();
            if (description.isEmpty()) description = product.getDescription();
            
            System.out.print("New Price (press Enter to keep current): ");
            String priceStr = scanner.nextLine();
            double price = priceStr.isEmpty() ? product.getPrice() : Double.parseDouble(priceStr);
            
            System.out.print("New Stock (press Enter to keep current): ");
            String stockStr = scanner.nextLine();
            int stock = stockStr.isEmpty() ? product.getStock() : Integer.parseInt(stockStr);
            
            // Category selection
            Category category = product.getCategory();
            List<Category> categories = Category.loadAllCategories();
            if (!categories.isEmpty()) {
                System.out.println("\nSelect a category:");
                for (int i = 0; i < categories.size(); i++) {
                    System.out.println((i + 1) + ". " + categories.get(i).getName());
                }
                System.out.println((categories.size() + 1) + ". Keep current category");
                System.out.print("Enter your choice: ");
                int catChoice = readIntInput();
                
                if (catChoice > 0 && catChoice <= categories.size()) {
                    category = categories.get(catChoice - 1);
                }
            }
            
            seller.updateProduct(product, name, description, price, stock, category);
            System.out.println("Product updated successfully!");
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    private static void removeProductMenu(Seller seller) {
        List<Product> products = seller.getProducts();
        if (products.isEmpty()) {
            System.out.println("You haven't added any products yet.");
            return;
        }
        
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.println((i + 1) + ". " + p.getName() + " - $" + p.getPrice());
        }
        
        System.out.print("Enter product number to remove: ");
        int choice = readIntInput();
        
        if (choice > 0 && choice <= products.size()) {
            Product product = products.get(choice - 1);
            seller.removeProduct(product);
            System.out.println("Product removed successfully!");
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    private static void addProductMenu(Seller seller) {
        System.out.println("\n=== ADD NEW PRODUCT ===");
        System.out.print("Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Price: ");
        double price = readDoubleInput();
        System.out.print("Stock: ");
        int stock = readIntInput();
        
        // Category selection
        Category category = null;
        List<Category> categories = Category.loadAllCategories();
        if (!categories.isEmpty()) {
            System.out.println("\nSelect a category:");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getName());
            }
            System.out.print("Enter your choice: ");
            int choice = readIntInput();
            
            if (choice > 0 && choice <= categories.size()) {
                category = categories.get(choice - 1);
            }
        } else {
            System.out.println("No categories available. Creating a default category.");
            category = new Category("CAT-1", "General", "General products");
            category.saveToFile();
        }
        
        String productId = "PRD-" + UUID.randomUUID().toString().substring(0, 8);
        Product product = seller.addProduct(productId, name, description, price, stock, category);
        System.out.println("Product added successfully!");
    }
    
    private static void viewSellerOrdersMenu(Seller seller) {
        List<Order> orders = seller.getReceivedOrders();
        if (orders.isEmpty()) {
            System.out.println("You haven't received any orders yet.");
            return;
        }
        
        System.out.println("\n=== RECEIVED ORDERS ===");
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            System.out.println((i + 1) + ". Order #" + order.getOrderId() + 
                               " - $" + order.getTotalAmount() + 
                               " (" + order.getStatus() + ")");
        }
        
        System.out.print("Enter order number to view details (0 to go back): ");
        int choice = readIntInput();
        
        if (choice > 0 && choice <= orders.size()) {
            Order order = orders.get(choice - 1);
            System.out.println("\n=== ORDER DETAILS ===");
            System.out.println(order);
            
            System.out.println("\n1. Update Order Status");
            System.out.println("2. Back to Orders");
            System.out.print("Enter your choice: ");
            
            int statusChoice = readIntInput();
            
            if (statusChoice == 1) {
                updateOrderStatusMenu(order);
            }
        }
    }
    
    private static void updateOrderStatusMenu(Order order) {
        System.out.println("\n=== UPDATE ORDER STATUS ===");
        System.out.println("1. Pending");
        System.out.println("2. Shipped");
        System.out.println("3. Delivered");
        System.out.println("4. Cancelled");
        System.out.print("Enter new status: ");
        
        int choice = readIntInput();
        String status;
        
        switch (choice) {
            case 1:
                status = "Pending";
                break;
            case 2:
                status = "Shipped";
                break;
            case 3:
                status = "Delivered";
                break;
            case 4:
                status = "Cancelled";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        order.setStatus(status);
        System.out.println("Order status updated to: " + status);
    }
    
    private static void runAdminMenu(Admin admin) {
        System.out.println("\n=== ADMIN MENU ===");
        System.out.println("1. Manage Categories");
        System.out.println("2. Manage Banners");
        System.out.println("3. View All Users");
        System.out.println("4. Logout");
        System.out.print("Enter your choice: ");
        
        int choice = readIntInput();
        
        switch (choice) {
            case 1:
                manageCategoriesMenu(admin);
                break;
            case 2:
                manageBannersMenu(admin);
                break;
            case 3:
                viewAllUsersMenu(admin);
                break;
            case 4:
                userManager.logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private static void manageCategoriesMenu(Admin admin) {
        System.out.println("\n=== MANAGE CATEGORIES ===");
        System.out.println("1. View Categories");
        System.out.println("2. Add New Category");
        System.out.println("3. Back to Menu");
        System.out.print("Enter your choice: ");
        
        int choice = readIntInput();
        
        switch (choice) {
            case 1:
                viewCategoriesMenu(admin);
                break;
            case 2:
                addCategoryMenu(admin);
                break;
            case 3:
                // Do nothing, just go back
                break;
        }
    }
    
    private static void viewCategoriesMenu(Admin admin) {
        List<Category> categories = Category.loadAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories available.");
            return;
        }
        
        System.out.println("\n=== CATEGORIES ===");
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            System.out.println((i + 1) + ". " + category.getName() + " - " + category.getDescription());
        }
        
        System.out.println("\n1. Edit Category");
        System.out.println("2. Remove Category");
        System.out.println("3. Back to Menu");
        System.out.print("Enter your choice: ");
        
        int choice = readIntInput();
        
        switch (choice) {
            case 1:
                editCategoryMenu(admin, categories);
                break;
            case 2:
                removeCategoryMenu(admin, categories);
                break;
            case 3:
                // Do nothing, just go back
                break;
        }
    }
    
    private static void editCategoryMenu(Admin admin, List<Category> categories) {
        System.out.print("Enter category number to edit: ");
        int choice = readIntInput();
        
        if (choice > 0 && choice <= categories.size()) {
            Category category = categories.get(choice - 1);
            
            System.out.println("\nEditing Category: " + category.getName());
            System.out.print("New Name (press Enter to keep current): ");
            String name = scanner.nextLine();
            if (name.isEmpty()) name = category.getName();
            
            System.out.print("New Description (press Enter to keep current): ");
            String description = scanner.nextLine();
            if (description.isEmpty()) description = category.getDescription();
            
            category.setName(name);
            category.setDescription(description);
            category.saveToFile();
            System.out.println("Category updated successfully!");
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    private static void removeCategoryMenu(Admin admin, List<Category> categories) {
        System.out.print("Enter category number to remove: ");
        int choice = readIntInput();
        
        if (choice > 0 && choice <= categories.size()) {
            Category category = categories.get(choice - 1);
            admin.removeCategory(category);
            System.out.println("Category removed successfully!");
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    private static void addCategoryMenu(Admin admin) {
        System.out.println("\n=== ADD NEW CATEGORY ===");
        System.out.print("Category Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        
        String categoryId = "CAT-" + UUID.randomUUID().toString().substring(0, 8);
        Category category = new Category(categoryId, name, description);
        admin.addCategory(category);
        System.out.println("Category added successfully!");
    }
    
    private static void manageBannersMenu(Admin admin) {
        System.out.println("\n=== MANAGE BANNERS ===");
        System.out.println("1. View Banners");
        System.out.println("2. Add New Banner");
        System.out.println("3. Back to Menu");
        System.out.print("Enter your choice: ");
        
        int choice = readIntInput();
        
        switch (choice) {
            case 1:
                viewBannersMenu(admin);
                break;
            case 2:
                addBannerMenu(admin);
                break;
            case 3:
                // Do nothing, just go back
                break;
        }
    }
    
    private static void viewBannersMenu(Admin admin) {
        List<Banner> banners = admin.getBanners();
        if (banners.isEmpty()) {
            System.out.println("No banners available.");
            return;
        }
        
        System.out.println("\n=== BANNERS ===");
        for (int i = 0; i < banners.size(); i++) {
            Banner banner = banners.get(i);
            System.out.println((i + 1) + ". " + banner.getTitle() + " [" + 
                               (banner.isActive() ? "ACTIVE" : "INACTIVE") + "]");
        }
        
        System.out.println("\n1. Edit Banner");
        System.out.println("2. Remove Banner");
        System.out.println("3. Back to Menu");
        System.out.print("Enter your choice: ");
        
        int choice = readIntInput();
        
        switch (choice) {
            case 1:
                editBannerMenu(admin, banners);
                break;
            case 2:
                removeBannerMenu(admin, banners);
                break;
            case 3:
                // Do nothing, just go back
                break;
        }
    }
    
    private static void editBannerMenu(Admin admin, List<Banner> banners) {
        System.out.print("Enter banner number to edit: ");
        int choice = readIntInput();
        
        if (choice > 0 && choice <= banners.size()) {
            Banner banner = banners.get(choice - 1);
            
            System.out.println("\nEditing Banner: " + banner.getTitle());
            System.out.print("New Title (press Enter to keep current): ");
            String title = scanner.nextLine();
            if (title.isEmpty()) title = banner.getTitle();
            
            System.out.print("New Content (press Enter to keep current): ");
            String content = scanner.nextLine();
            if (content.isEmpty()) content = banner.getContent();
            
            System.out.print("Active (y/n): ");
            String activeStr = scanner.nextLine();
            boolean active = activeStr.equalsIgnoreCase("y");
            
            banner.setTitle(title);
            banner.setContent(content);
            banner.setActive(active);
            banner.saveToFile();
            System.out.println("Banner updated successfully!");
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    private static void removeBannerMenu(Admin admin, List<Banner> banners) {
        System.out.print("Enter banner number to remove: ");
        int choice = readIntInput();
        
        if (choice > 0 && choice <= banners.size()) {
            Banner banner = banners.get(choice - 1);
            admin.removeBanner(banner);
            System.out.println("Banner removed successfully!");
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    private static void addBannerMenu(Admin admin) {
        System.out.println("\n=== ADD NEW BANNER ===");
        System.out.print("Banner Title: ");
        String title = scanner.nextLine();
        System.out.print("Content: ");
        String content = scanner.nextLine();
        
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(30); // Default 30 days duration
        
        Banner banner = new Banner(title, content, startDate, endDate);
        admin.addBanner(banner);
        System.out.println("Banner added successfully!");
    }
    
    private static void viewAllUsersMenu(Admin admin) {
        System.out.println("\n=== VIEW USERS ===");
        System.out.println("1. View Buyers");
        System.out.println("2. View Sellers");
        System.out.println("3. View Admins");
        System.out.println("4. Back to Menu");
        System.out.print("Enter your choice: ");
        
        int choice = readIntInput();
        
        switch (choice) {
            case 1:
                List<Buyer> buyers = userManager.getAllBuyers();
                System.out.println("\n=== BUYERS ===");
                if (buyers.isEmpty()) {
                    System.out.println("No buyers registered.");
                } else {
                    for (Buyer buyer : buyers) {
                        System.out.println("- " + buyer.getUsername() + " (" + buyer.getEmail() + ")");
                    }
                }
                break;
            case 2:
                List<Seller> sellers = userManager.getAllSellers();
                System.out.println("\n=== SELLERS ===");
                if (sellers.isEmpty()) {
                    System.out.println("No sellers registered.");
                } else {
                    for (Seller seller : sellers) {
                        System.out.println("- " + seller.getUsername() + " (" + seller.getEmail() + 
                                           ") - Rating: " + seller.getRating());
                    }
                }
                break;
            case 3:
                List<Admin> admins = userManager.getAllAdmins();
                System.out.println("\n=== ADMINS ===");
                if (admins.isEmpty()) {
                    System.out.println("No other admins registered.");
                } else {
                    for (Admin otherAdmin : admins) {
                        System.out.println("- " + otherAdmin.getUsername() + " (" + otherAdmin.getEmail() + ")");
                    }
                }
                break;
            case 4:
                // Do nothing, just go back
                break;
        }
    }
    
    private static int readIntInput() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 0; // Default value for invalid input
        }
    }
    
    private static double readDoubleInput() {
        try {
            String input = scanner.nextLine().trim();
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return 0.0; // Default value for invalid input
        }
    }
    
    private static void createDemoData() {
        // Create demo admin if not exists
        if (userManager.login("admin", "admin123") == null) {
            Admin admin = userManager.registerAdmin("admin", "admin123", "admin@marketplace.com", "123-456-7890", "Admin HQ");
            userManager.logout();
            
            // Create demo categories
            Category electronics = new Category("CAT-1", "Electronics", "Electronic devices and gadgets");
            Category clothing = new Category("CAT-2", "Clothing", "Apparels and fashion items");
            Category books = new Category("CAT-3", "Books", "Books and publications");
            
            electronics.saveToFile();
            clothing.saveToFile();
            books.saveToFile();
            
            // Create a demo banner
            LocalDateTime now = LocalDateTime.now();
            Banner welcomeBanner = new Banner("Welcome to Marketplace", 
                                             "Shop our exclusive collection of products with great deals!", 
                                             now, now.plusMonths(1));
            admin.addBanner(welcomeBanner);
        }
        
        // Create demo seller if not exists
        if (userManager.login("seller", "seller123") == null) {
            Seller seller = userManager.registerSeller("seller", "seller123", "seller@marketplace.com", "234-567-8901", "Seller Shop");
            userManager.logout();
            
            // Add demo products
            List<Category> categories = Category.loadAllCategories();
            if (!categories.isEmpty()) {
                seller.addProduct("PRD-1", "Smartphone", "Latest model with advanced features", 699.99, 20, categories.get(0));
                seller.addProduct("PRD-2", "T-shirt", "Cotton comfortable t-shirt", 24.99, 50, categories.get(1));
                seller.addProduct("PRD-3", "Java Programming", "Learn Java programming language", 49.99, 30, categories.get(2));
            }
        }
        
        // Create demo buyer if not exists
        if (userManager.login("buyer", "buyer123") == null) {
            userManager.registerBuyer("buyer", "buyer123", "buyer@marketplace.com", "345-678-9012", "Buyer Home");
            userManager.logout();
        }
    }
}
