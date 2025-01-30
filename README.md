# 🎬 Movie Reservation System (Spring Boot + PostgreSQL)

## 📌 Overview
This is a **backend system** for a movie reservation service built with **Spring Boot**, **PostgreSQL**, **Hibernate**, and **JWT authentication**. The system allows users to **sign up, log in, browse movies, reserve seats, and manage their reservations**. Admin users can **add/update/delete movies, manage showtimes, and view reports**.

## 🚀 Features
### **User Authentication & Authorization**
- User registration and login (JWT-based authentication)
- Role-based access control (**Admin & Regular User**)
- Password hashing using **BCrypt**

### **Movie & Showtime Management (Admin)**
- Add, update, delete movies
- Manage showtimes for different movies

### **Seat Reservation (User)**
- View available movies and showtimes
- Reserve seats for a showtime
- Cancel upcoming reservations

### **Reporting (Admin)**
- View total reservations per showtime
- Check seat availability and occupancy rate
- Calculate revenue from reservations

## 🏗️ Tech Stack
- **Backend:** Java (Spring Boot)
- **Database:** PostgreSQL
- **ORM:** Hibernate (Spring Data JPA)
- **Authentication:** JWT (JSON Web Token)
- **Caching:** Redis (for seat availability and optimization)
- **Security:** Spring Security + BCrypt Password Encoding

## 🎯 Database Schema (Entities)
### **1. User**
Manages user authentication and roles.
```java
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role; // USER, ADMIN
}
```

### **2. Movie**
Stores movie details.
```java
@Entity
@Table(name = "movies")
public class Movie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String genre;
    private String description;
}
```

### **3. Showtime**
Handles movie scheduling.
```java
@Entity
@Table(name = "showtimes")
public class Showtime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Movie movie;
    private LocalDateTime showtime;
    private int totalSeats;
}
```

### **4. Seat**
Tracks seat availability for showtimes.
```java
@Entity
@Table(name = "seats")
public class Seat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Showtime showtime;
    private String seatNumber;
    @Enumerated(EnumType.STRING)
    private SeatStatus status; // AVAILABLE, RESERVED
}
```

### **5. Reservation**
Handles user seat bookings.
```java
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Showtime showtime;
    @OneToOne
    private Seat seat;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status; // CONFIRMED, CANCELED
}
```

## ⚙️ Installation & Setup
### **1. Clone the Repository**
```sh
git clone https://github.com/your-username/movie-reservation-system.git
cd movie-reservation-system
```

### **2. Configure Database (PostgreSQL)**
Create a new database in PostgreSQL:
```sql
CREATE DATABASE movie_reservation;
```
Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/movie_reservation
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
```

### **3. Run the Application**
```sh
mvn spring-boot:run
```

## 🛠 API Endpoints
### **Authentication APIs**
| Method | Endpoint         | Description         |
|--------|----------------|--------------------|
| POST   | `/api/auth/signup`  | Register a new user |
| POST   | `/api/auth/login`   | User login (JWT) |

### **Movie Management APIs (Admin)**
| Method | Endpoint           | Description            |
|--------|-------------------|-----------------------|
| POST   | `/api/movies`       | Add a new movie       |
| PUT    | `/api/movies/{id}`  | Update a movie       |
| DELETE | `/api/movies/{id}`  | Delete a movie       |
| GET    | `/api/movies`       | List all movies      |

### **Showtime APIs**
| Method | Endpoint              | Description              |
|--------|----------------------|-------------------------|
| GET    | `/api/showtimes`       | List all showtimes     |
| POST   | `/api/showtimes`       | Add a new showtime     |

### **Reservation APIs (User)**
| Method | Endpoint                  | Description                  |
|--------|--------------------------|------------------------------|
| POST   | `/api/reservations`        | Reserve a seat              |
| GET    | `/api/reservations`        | List user reservations      |
| DELETE | `/api/reservations/{id}`   | Cancel a reservation        |

## 📜 License
This project is licensed under the **MIT License**.

## 📌 Future Improvements
- **Payment Integration** (Stripe, PayPal, etc.)
- **Email Notifications** for booking confirmations
- **Real-time Seat Availability Updates** using WebSockets

## 🤝 Contribution
Feel free to fork this repository and contribute!

🚀 **Happy Coding!**

