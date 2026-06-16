## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).


# SPOMAP - E-Commerce Application

A Java Swing-based e-commerce desktop application with a complete shopping experience, user authentication, and product management.

## 📋 Overview

SPOMAP is a desktop e-commerce application built with Java Swing following the Model-View-Controller (MVC) architecture. It provides a complete shopping experience including product browsing, shopping cart management, user authentication with different user types (Admin, Normal, Prime), and order tracking.

## ✨ Features

- **Product Catalog**: Browse products with detailed information, images, and specifications
- **Search & Filter**: Advanced product search and filtering capabilities
- **Shopping Cart**: Add/remove items, adjust quantities, and manage your cart
- **User Authentication**: Support for multiple user types:
  - Admin Users
  - Normal Users
  - Prime Users (with special benefits)
- **Product Views**: Multiple image views per product with color variants
- **Discount System**: Product discounts and special pricing
- **Navigation**: Sidebar navigation and multi-view panel system

## 🏗️ Project Structure

```
spomap/
├── src/                    # Source code
│   ├── Main.java          # Application entry point
│   ├── MainFrame.java     # Main application window
│   ├── Controller/        # MVC Controllers
│   │   ├── AppController.java
│   │   ├── AuthenticationController.java
│   │   ├── NavigationController.java
│   │   ├── ProductController.java
│   │   ├── ShopController.java
│   │   ├── ShoppingCartController.java
│   │   └── SidebarController.java
│   ├── Model/             # MVC Models
│   │   ├── User.java (AdminUser, NormalUser, PrimeUser)
│   │   ├── Product.java
│   │   ├── ProductCatalog.java
│   │   ├── ShoppingCart.java
│   │   ├── CartItem.java
│   │   └── ...
│   ├── View/              # MVC Views
│   │   ├── ShopView.java
│   │   ├── ProductView.java
│   │   ├── ShoppingCartView.java
│   │   ├── AuthenticationView.java
│   │   ├── NavigationView.java
│   │   └── SidebarView.java
│   ├── Components/        # UI Components
│   │   └── MultiViewPanel.java
│   ├── Service/           # Business Logic Services
│   └── Util/              # Utility Classes
├── lib/                    # External Dependencies
│   ├── jackson-annotations-2.22.jar
│   ├── jackson-core-2.21.4.jar
│   ├── jackson-databind-2.22.0.jar
│   └── jackson-datatype-jsr310-2.7.0.jar
├── database/               # Data Storage
│   ├── products.json      # Product catalog data
│   ├── admin_users.json   # Admin user accounts
│   ├── normal_users.json  # Normal user accounts
│   └── pictures/          # Product images
└── icons/                  # Application icons and UI assets
```

## 🛠️ Technologies

- **Language**: Java
- **GUI Framework**: Java Swing
- **JSON Processing**: Jackson (FasterXML)
- **Architecture**: Model-View-Controller (MVC)
- **Build Tool**: Compatible with standard Java build tools

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Jackson libraries (included in `lib/` folder)

### Building the Project

1. Clone the repository:

```bash
git clone <repository-url>
cd spomap
```

2. Compile the source files:

```bash
javac -cp "lib/*" -d bin src/*.java src/**/*.java
```

### Running the Application

```bash
java -cp "bin:lib/*" Main
```

Or on Windows:

```bash
java -cp "bin;lib/*" Main
```

## 📦 Dependencies

The project uses the following external libraries (included in `lib/`):

- **Jackson Annotations** (2.22) - Metadata annotations for Jackson
- **Jackson Core** (2.21.4) - Core JSON processing
- **Jackson Databind** (2.22.0) - Data binding functionality
- **Jackson Datatype JSR310** (2.7.0) - Java 8 Date/Time support

## 💾 Database

The application uses JSON files for data storage:

- **products.json**: Contains product catalog with details like name, price, description, images, colors, manufacturer, and technical specifications
- **admin_users.json**: Administrator account information
- **normal_users.json**: Regular user account information
- **pictures/**: Directory containing product images

## 🎨 User Types

### Admin User

- Full access to system features
- Can manage products and users

### Normal User

- Standard shopping features
- Browse products and manage cart

### Prime User

- Enhanced benefits and discounts
- Priority features

## 🖼️ Assets

The `icons/` directory contains:

- Application logos (SPOMAP branding)
- UI icons (search, cart, settings, notifications, etc.)
- User avatars
- Navigation elements (arrows, plus/minus buttons)

## 📝 License

This project is licensed under the terms specified in the [LICENSE](LICENSE) file.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.

## 📧 Contact

For questions or support, please open an issue in the repository.
