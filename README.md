# 📚 Lumina Books

A modern **Jakarta EE** web application for purchasing, reviewing, and downloading digital books. Built using **Java, Jakarta REST (JAX-RS), Hibernate ORM, Maven, MySQL, and an Embedded Apache Tomcat Server**, the project follows the **MVC architectural pattern** and demonstrates enterprise-level web application development practices.

> Developed as a **Web Programming II Viva Project**.

---

# ✨ Features

## 👤 Customer

- User Registration & Authentication
- Browse Latest E-Books
- Browse Books by Genre
- Basic Search
- Advanced Search (Genre, Author & Price)
- View Detailed Book Information
- Shopping Cart Management
- Secure Checkout
- Integrated Payment Gateway
- Order History
- Invoice Generation
- Personal Digital Library
- Download Purchased E-Books
- Book Reviews & Ratings
- Personalized Book Recommendation System
- Email Notifications
- User Profile Management

## 🛠 Administrator

- Admin Dashboard
- User Management
- Genre Management
- Book Management
- Order Management
- Manage Reviews & Ratings
- Manage Book Recommendations
- Activate / Deactivate Books

---

# 🏗 System Architecture

The project follows a clean layered MVC architecture with RESTful APIs.

```
                  Client (Browser)
                         │
                         ▼
          HTML • CSS • JavaScript Frontend
                         │
                         ▼
          Jakarta REST Controllers (JAX-RS)
                         │
                         ▼
               Service Layer (Business Logic)
                         │
                         ▼
                  DTO Transformation Layer
                         │
                         ▼
              Hibernate ORM (Persistence)
                         │
                         ▼
                    MySQL Database
```

---

# 🛠 Tech Stack

### Backend

- Java 17
- Jakarta EE
- Jakarta REST (JAX-RS)
- Hibernate ORM
- Maven
- Embedded Apache Tomcat

### Frontend

- HTML5
- CSS3
- JavaScript

### Database

- MySQL

### Data Exchange

- JSON

### Development Tools

- IntelliJ IDEA
- Git
- GitHub

---

# 📁 Project Structure

```
src/
│
├── controller/
├── service/
├── dto/
├── entity/
├── util/
├── filter/
├── config/
└── resources/

web/
│
├── css/
├── js/
├── images/
└── pages/
```

---

# 📖 Core Modules

## Authentication

- User Registration
- Secure Login
- Session Management
- Role-Based Authorization

## Book Management

- Browse Books
- Genre Filtering
- Search Books
- Advanced Search
- View Book Details

## Shopping Cart

- Add Books
- Remove Books
- Clear Cart
- Persistent User Cart

## Orders & Checkout

- Checkout Process
- Payment Processing
- Invoice Generation
- Order History

## Digital Library

- Download Purchased Books
- Purchase Verification

## Reviews & Ratings

- Submit Reviews
- Book Ratings
- Review Management

## Recommendation System

- Personalized Book Suggestions
- Related Book Recommendations

## Notifications

- Email Notifications
- Purchase Confirmation Emails

---

# 🌐 REST API Overview

| Method | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/books` | Browse latest books |
| GET | `/api/books/{id}` | View book details |
| GET | `/api/books/search` | Search books |
| GET | `/api/genres` | Load genres |
| GET | `/api/cart` | View shopping cart |
| POST | `/api/cart/add` | Add book to cart |
| POST | `/api/cart/remove` | Remove book from cart |
| POST | `/api/cart/clear` | Clear shopping cart |
| POST | `/api/checkout` | Checkout cart |
| GET | `/api/orders` | View order history |
| GET | `/api/orders/{id}` | View invoice |
| GET | `/api/download/{ebookId}` | Download purchased book |

---

# 🔒 Security Features

- Secure User Authentication
- Role-Based Authorization
- Session Management
- Input Validation
- Hibernate ORM Protection Against SQL Injection
- Secure Purchase Verification
- Transactional Checkout Processing
- JSON-based REST Communication
- Proper HTTP Response Handling

---

# ⚙ Requirements

- Java JDK 17 or later
- Maven
- MySQL Server
- Modern Web Browser
- Embedded Apache Tomcat

---

# 🚀 Getting Started

## 1. Clone the Repository

```bash
git clone https://github.com/yourusername/lumina-books.git
```

## 2. Navigate into the Project

```bash
cd lumina-books
```

## 3. Configure the Database

- Create a MySQL database.
- Update the database configuration with your credentials.

## 4. Build the Project

```bash
mvn clean install
```

## 5. Run the Application

```bash
mvn tomcat7:run
```

or run using your embedded Tomcat configuration.

---

# 📸 Screenshots

> Add screenshots here.

Suggested screenshots:

- Login
- Register
- Home
- Search
- Book Details
- Shopping Cart
- Checkout
- Payment Success
- Invoice
- My Library
- Book Reviews
- Recommendation Section
- User Dashboard
- Admin Dashboard
- Book Management
- Order Management

---

# 🎯 Learning Outcomes

This project demonstrates practical experience with:

- Jakarta EE
- RESTful API Development (JAX-RS)
- Hibernate ORM
- MVC Architecture
- DTO Pattern
- Maven Project Structure
- Embedded Tomcat Deployment
- Session Management
- Authentication & Authorization
- Role-Based Access Control
- Enterprise Java Development
- E-Commerce System Design
- Payment Gateway Integration
- Email Service Integration
- Recommendation System Design
- Review & Rating System
- Digital File Management
- JSON-based Client–Server Communication
- MySQL Database Design

---

# 🚀 Future Improvements

- JWT Authentication
- OAuth Login (Google & GitHub)
- Docker Containerization
- CI/CD Pipeline using GitHub Actions
- AWS Cloud Deployment
- Redis Caching
- Elasticsearch Integration
- Multi-language Support
- Analytics Dashboard
- Unit & Integration Testing
- Kubernetes Deployment

---

# 🤝 Contributing

Contributions, issues, and feature requests are welcome.

If you'd like to improve this project:

1. Fork the repository.
2. Create a new feature branch.
3. Commit your changes.
4. Open a Pull Request.

---

# 📄 License

This project is developed for educational purposes.

---

# 👨‍💻 Author

**Pulesh Aponso**

Professional Diploma in Software Engineering

GitHub: **https://github.com/yourusername**

---

## ⭐ If you found this project useful, consider giving it a star!
