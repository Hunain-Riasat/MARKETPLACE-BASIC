# 🛒 MARKETPLACE-III

An E-Commerce Marketplace for buying and selling goods.

---

## 📚 Java Marketplace Project
A simple object-oriented, console-based marketplace application where users can register as buyers or sellers, add products, place orders, and manage marketplace data.

---

## 👨‍💻 Presented By **HSH Group**
- **M Hunain Riasat** — FA24-BSE-083
- **Saad Faisal** — FA24-BSE-104
- **Hannan Najeeb** — FA24-BSE-080

---

## 📁 Project Structure
MARKETPLACE-III/

│

├── data/ # Persistent data storage (created/used by the app)

│ ├── buyers/

│ ├── orders/

│ ├── products/

│ ├── sellers/

│ └── users/

│

├── src/ # Java source code

│ └── marketplace/ # All Java classes (Main.java, User.java, etc.)

│

├── README.md # Project documentation


> ⚡ **Note:**  
> Folders like `admins`, `banners`, `buyers`, `categories`, `products`, `reviews`, `sellers` inside `src/` are **not required** and can be deleted.  
> Only `src/marketplace/` should contain your `.java` source files.

---

## 🚀 Features
- User registration and login (buyer, seller, admin)
- Product management (add, update, delete products)
- Shopping cart and wishlist for buyers
- Order placement and payment processing
- Product reviews and ratings
- Category and banner management (admin)
- File-based data persistence (no external database required)
- Console-based interactive menu

---

## 🛠️ Requirements
- Java JDK **17+** (Recommended: **JDK 21** or **JDK 23**)
- Console/Terminal
- No external libraries required

---

## 📝 How to Run

1. **Clone or Download** the project.
2. **Ensure Java version is 17 or higher**:
    ```bash
    java -version
    javac -version
    ```
3. **Open Terminal** in the project root.

4. **Compile the project**:
    ```bash
    cd src
    javac marketplace/*.java
    ```

5. **Run the application**:
    ```bash
    java marketplace.Main
    ```

---

## 🧩 Usage
- Register as a buyer or seller (or use demo accounts if demo data is enabled).
- Login and use the menu to:
  - Browse products
  - Manage your cart
  - Place orders
  - Add products as a seller
- Admin can manage:
  - Categories
  - Banners
  - View all users

---

## 🗂️ Data Storage
- All persistent data (users, products, orders, etc.) is saved in the `data/` folder at the project root.
- Make sure **no data folders are inside `src/`**.  
  Only `.java` source files should be inside `src/marketplace/`.

---

## 🧑‍💻 OOP Concepts Demonstrated
- **Encapsulation:**  
  All fields are private with getters/setters.
- **Inheritance:**  
  `Buyer`, `Seller`, and `Admin` extend the `User` class.
- **Association:**  
  `Cart`, `Wishlist`, `Orders`, and `Reviews` are associated with users and products.
- **File Handling:**  
  Java serialization for data persistence.

---

## 📢 Notes
- If you modify file handling paths, ensure all data is saved/loaded from the `data/` directory at the project root.
- Keep the `src/` folder for **source code only** (no data files inside).

---

## 📞 Contact
For questions or issues, contact:  
📧 **Hunain Riasat** — [hunainriasat@gmail.com]
📧 **Saad faisal** — [fa24-bse-104@cuilahore.edu.pk]
📧 **Hannan Najeeb** — [fa24-bse-080@cuilahore.edu.pk]

---

🎉 **Enjoy using the Java Marketplace!**
