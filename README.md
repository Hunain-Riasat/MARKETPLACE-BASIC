# MARKETPLACE-III
AN ECOMMERECE MARKETPLACE FOR BUYING AND SELLING GOODS..

Java Marketplace Project
A simple object-oriented console-based marketplace application where users can register as buyers or sellers, add products, place orders, and manage marketplace data.

Presented By HSH Groups;
M HUNAIN RIASAT   FA24-BSE-083
SAAD FAISAL       FA24-BSE-104
HANNAN NAJEEB     FA24-BSE-080

📁 Project Structure
text
MARKETPLACE-III/
│
├── data/                   # Serialized user, product, and order data
│   ├── buyers/
│   ├── orders/
│   ├── products/
│   ├── sellers/
│   └── users/
│
├── src/                    # Java source code
│   └── marketplace/        # All Java classes (Main, User, Buyer, Seller, etc.)
│
├── README.md               # Project documentation
Note:
Folders like admins, banners, buyers, categories, products, reviews, sellers inside src/ are not required and can be deleted.
Only src/marketplace/ should contain your .java source files.

🚀 Features
User registration and login (buyer, seller, admin)

Product management (add, update, delete products)

Shopping cart and wishlist for buyers

Order placement and payment processing

Product reviews and ratings

Category and banner management (admin)

File-based data persistence (no external database required)

Console-based interactive menu

🛠️ Requirements
Java JDK 17+ (JDK 21 or 23 recommended)

Console/Terminal

No external libraries required

📝 How to Run
Clone or download the project.

Ensure your Java version is 17 or higher:

text in cmd / terminal
java -version
javac -version
Open a terminal in the project root.

Compile the project:


cd src
javac marketplace/*.java
Run the application:

text in cmd / terminal
java marketplace.Main
🧩 Usage
Register as a buyer or seller (or use demo accounts if demo data is enabled).

Login and use the menu to browse products, manage your cart, place orders, or add products.

Admin can manage categories, banners, and view all users.

🗂️ Data Storage
All persistent data (users, products, orders, etc.) is saved in the data/ folder at the project root.

If you see folders like admins, buyers, etc. inside src/, you can safely delete them.
Only the marketplace folder inside src/ should contain your Java source files.



🧑‍💻 OOP Concepts Demonstrated
Encapsulation:  All fields are private with getters/setters.
Inheritance:    Buyer, Seller, and Admin extend User.
Association:    Cart, Wishlist, Orders, and Reviews are associated with users and products.
File Handling:  Java serialization for data persistence.



📢 Notes
If you modify file handling paths, ensure all data is saved/loaded from the data/ directory at the project root.

Do not place any data folders inside src/; keep src/ for source code only.



📞 Contact
For questions or issues, contact 
Hunain Riasat at [hunainriasat@gmail.com].

Enjoy using the Java Marketplace!



