# VYBK Matchday Arena — 3D Stadium Ticket Booking

A full-stack **3D Stadium Ticket Booking** web application inspired by **Vivekananda Yuba Bharati Krirangan (Salt Lake Stadium), Kolkata**, built with **Spring Boot 3 (Java 17, Maven, Spring Data JPA, H2/MySQL)** and a single-file **Three.js + Tailwind CSS + GSAP** interactive 3D frontend.

## Tech Stack

- **Backend:** Java 17, Spring Boot 3.3.5, Spring Web, Spring Data JPA, Bean Validation, H2 Database (default), MySQL Connector/J
- **Frontend:** Self-contained HTML5, Tailwind CSS (CDN), Three.js (ES Modules, `InstancedMesh`, `OrbitControls`, `ACESFilmicToneMapping`, `PMREMGenerator`), GSAP 3.12

## Project Structure

```text
├── pom.xml
├── .gitignore
├── README.md
└── src/main/
    ├── java/com/stadium/booking/
    │   ├── StadiumBookingApplication.java
    │   ├── controller/SeatBookingController.java
    │   ├── dto/SeatBookingDtos.java
    │   ├── entity/SeatBooking.java
    │   ├── exception/
    │   │   ├── GlobalExceptionHandler.java
    │   │   └── SeatAlreadyBookedException.java
    │   ├── repository/SeatBookingRepository.java
    │   └── service/SeatBookingService.java
    └── resources/
        ├── application.properties
        └── static/index.html
```

## Running the Application

1. Start the Spring Boot server:
   ```bash
   mvn spring-boot:run
   ```
2. Open the 3D Stadium UI in your browser:
   - **App URL:** http://localhost:8080
   - **H2 Console:** http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:stadiumdb`, User: `sa`, blank password)

### Switching to MySQL

Edit `src/main/resources/application.properties`: comment out the H2 properties and uncomment the MySQL datasource block with your MySQL username and password.

## REST API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/seats` | Returns booked seat statuses (`id`, `block`, `rowNumber`, `seatNumber`, `price`, `status`). Optional `?block=` filter. |
| `POST` | `/api/seats/book` | Books a seat (enforces unique constraint on `block + rowNumber + seatNumber`; returns `409 Conflict` if taken). |
| `GET` | `/api/seats/tickets?email=...` | Retrieves all tickets for the given customer email, ordered newest first. |

### Example `POST /api/seats/book` Payload

```json
{
  "block": "NORTH-MIDDLE",
  "rowNumber": 5,
  "seatNumber": 45,
  "price": 1500,
  "customerName": "Trishir Mondal",
  "customerEmail": "trishir@example.com"
}
```

## 3D Features

- **VYBK Architecture:** Golden-hour Kolkata sky dome with horizon haze, `PMREMGenerator` reflections, 8-lane terracotta athletics track, striped FIFA pitch with goals and corner flags, glowing LED perimeter boards, twin scoreboards, cream exterior facade with curved pedestrian ramps, ribbed white cantilever roof with floodlights, Salt Lake water reflection, and distant skyline.
- **High-Density Instanced Seating & Mosaics:** Sculpted 3D bucket seats rendered via a single `THREE.InstancedMesh` sized dynamically to ~93% of elliptical arc column spacing, featuring double-stroke **`MBSG`** (Mohun Bagan green & maroon North Stand) and **`TRISHIR`** (navy/slate South Stand) Middle Tier mosaics.
- **Interactive Booking Flow:** 6-second GSAP cinema-drone intro (with Skip & Replay), stand quick-views, seat search, floating ticket preview card with ₹ INR pricing, 360° first-person **View from Seat** POV mode, live checkout with `409 Conflict` handling, printable Digital Matchday Pass, and **My Tickets** drawer.
