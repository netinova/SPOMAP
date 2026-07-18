<div align="center">

<img src="icons/logo_svg/SPOMAP_BGblack_LogoRedame.svg" alt="SPOMAP Logo" width="300"/>

# 🛍️ SPOMAP - Smart Point of Sale & Marketplace

_A powerful, modern desktop application for managing your shop with ease_

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Status](https://img.shields.io/badge/status-Active-success.svg)]()

</div>

---

## ✨ Features

- 🔐 **Multi-tier User System** - Admin, Prime, and Normal user roles with different permissions
- 🛒 **Shopping Cart Management** - Easy-to-use cart with quantity controls
- 📦 **Product Catalog** - Browse and search products with detailed technical specifications
- 💳 **Invoice Management** - Complete billing and order tracking system
- 👤 **User Profiles** - Manage user accounts and wallet charges
- 🎨 **Theme Support** - Dark and light themes with customizable color schemes
- 📊 **Shop Analytics** - Track sales and business metrics
- 🔍 **Advanced Search** - Find products and invoices quickly
- 💰 **Wallet System** - Charge and manage user wallets

---

## 📸 Screenshots

<div align="center">

### Shopping View

![Dashboard](screenshots/SPOMAP%202026-07-18%204_06_25%20PM.png)

### Product Browsing

![Products](screenshots/SPOMAP%202026-07-18%204_07_50%20PM.png)

### Shop Analytics

![Shopping](screenshots/SPOMAP%202026-07-18%204_09_03%20PM.png)

### Login Screen

![Login](screenshots/Screenshot%202026-07-18%20160829.png)

</div>

---

## 🚀 Quick Start

### System Requirements

- **Java Runtime Environment (JRE)** 17 or higher
- **Operating System**: Windows, macOS, or Linux
- **RAM**: Minimum 512 MB (recommended 1 GB)
- **Disk Space**: ~100 MB

### Installation

#### Clone the Repository

```bash
git clone https://github.com/netinova/SPOMAP.git
cd SPOMAP
```

---

## 📖 How to Run

### Prerequisites

Make sure you have Java 17+ installed:

```bash
java -version
```

If not installed, download from [Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/)

### Command Line

#### Windows

```bash
cd path/to/SPOMAP
javac -source 17 -target 17 -d bin -cp "lib/*;src" src/Main.java src/MainFrame.java src/Model/*.java src/Util/*.java src/Components/*.java src/Controller/*.java src/View/*.java src/Service/*.java
java -cp "bin;lib/*" Main
```

#### macOS / Linux

```bash
cd path/to/SPOMAP
javac -source 17 -target 17 -d bin -cp "lib/*:src" src/Main.java src/MainFrame.java src/Model/*.java src/Util/*.java src/Components/*.java src/Controller/*.java src/View/*.java src/Service/*.java
java -cp "bin:lib/*" Main
```

### Using an IDE (Recommended)

1. **IntelliJ IDEA**
   - Open the project folder
   - Set up Java SDK (17+)
   - Right-click `src/Main.java`
   - Select "Run 'Main.main()'"

2. **VS Code**
   - Install Extension Pack for Java
   - Open the folder
   - Click Run on `Main.java`

---

## 🔑 Default Credentials

Test the application with these demo accounts:

| Role   | Username    | Password |
| ------ | ----------- | -------- |
| Admin  | 09111111111 | 1        |
| Prime  | 09123456789 | 1        |
| Normal | 09123123122 | 1        |

> ⚠️ Change these credentials before deploying to production!

---

## 📁 Project Structure

```
SPOMAP/
├── src/
│   ├── Main.java                 # Application entry point
│   ├── MainFrame.java            # Main application window
│   ├── Components/               # Reusable UI components
│   ├── Controller/               # Business logic controllers
│   ├── Model/                    # Data models
│   ├── Service/                  # Service layer
│   ├── Util/                     # Utility classes
│   └── View/                     # View classes
├── database/
│   ├── admin_users.json         # Admin user data
│   ├── normal_users.json        # Normal user data
│   ├── prime_users.json         # Prime user data
│   ├── products.json            # Product catalog
│   ├── invoices.json            # Invoice records
│   ├── settings.json            # Application settings
│   ├── shop_analytics.json      # Analytics data
│   └── themes.json              # Theme configurations
├── lib/                          # External libraries
├── icons/                        # Application icons
├── screenshots/                  # Screenshot documentation
└── README.md                     # This file
```

---

## 🎯 Key Components

### User Management

- **Authentication System**: Secure login with role-based access control
- **Admin Panel**: Manage users, products, and system settings
- **User Profiles**: Edit personal information and manage preferences

### Shopping Features

- **Product Search**: Multi-criteria search with filters
- **Shopping Cart**: Add/remove items with quantity controls
- **Checkout**: Complete purchase with invoice generation
- **Order History**: View past transactions and invoices

### Admin Features

- **Product Management**: Add, edit, and delete products
- **User Management**: Create and manage user accounts
- **Analytics Dashboard**: View sales metrics and shop performance
- **Settings Panel**: Customize application behavior

### Customization

- **Theme Engine**: Switch between dark and light themes
- **Custom Themes**: Add and Modify your themes in `database/themes.json`
- **Settings**: Store preferences in `database/settings.json`

---

## 🎨 Architecture

### Design Pattern

SPOMAP follows the **Model-View-Controller (MVC)** architecture:

- **Model**: Data structures and business logic
- **View**: GUI components and panels
- **Controller**: Event handling and user interactions

### Technology Stack

- **Language**: Java 17+
- **GUI Framework**: Swing
- **Data Storage**: JSON (with Jackson)
- **Build**: Manual javac compilation

---

## 🔐 Security

- Role-based access control (Admin, Prime, Normal)
- User authentication system
- Secure wallet management
- Order history protection

---

## 🐛 Troubleshooting

### "Java is not recognized"

- Install Java JDK from [oracle.com](https://www.oracle.com/java/)
- Add Java to your system PATH

### Application won't start

- Verify Java version: `java -version` (should be 17+)
- Check that all dependencies in `lib/` folder are present
- Ensure database folder and JSON files exist

### GUI looks wrong

- Try different themes: Settings → Theme Picker
- Check monitor resolution settings
- Update your Java installation

---

## 📝 Development Guide

### Adding a New Feature

1. Create model classes in `src/Model/`
2. Add business logic in `src/Service/`
3. Create UI components in `src/Components/`
4. Add controller logic in `src/Controller/`
5. Update the main view in `src/View/`

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🤝 Contributing

Contributions are welcome! Feel free to:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📞 Support & Contact

- 📧 Email: [manibaghdadi484@gmail.com]
- 🐛 Report Issues: [GitHub Issues]
- 💬 Discussions: [GitHub Discussions]

---

## 🙏 Acknowledgments

- Built with ❤️ using Java Swing
- Thanks to all contributors
- Icons designed with SVG

---

<div align="center">

**Made by SPOMAP Team**

⭐ If you found this helpful, please give us a star!

</div>
