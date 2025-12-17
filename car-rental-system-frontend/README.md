# 🚗 Car Rental System

A robust and modern full-stack car rental application designed to streamline vehicle booking and management. Built with **Spring Boot** for the backend and **React** for the frontend, this system offers a seamless experience for both customers and administrators.

![Project Banner](https://placehold.co/1200x400?text=Car+Rental+System+Preview)
*(Replace this link with a real screenshot of your application)*

---

## 🌟 Features

### 👤 User Panel
- **User Authentication:** Secure registration and login (JWT-based).
- **Vehicle Browsing:** View available cars with detailed descriptions and images.
- **Booking System:** Easy-to-use interface to book vehicles for specific dates.
- **My Bookings:** track status of past and current reservations.
- **Responsive Design:** Fully optimized for desktop and mobile devices.

### 🛡️ Admin Dashboard
- **Dashboard Overview:** key metrics and statistics.
- **Car Management:** Add, update, and remove vehicles from the fleet.
- **Booking Management:** Approve or reject customer reservations.
- **User Management:** Oversee registered users.

---

## 🛠️ Tech Stack

### Frontend
- **Framework:** [React](https://reactjs.org/) (v18)
- **Styling:** [Bootstrap 5](https://getbootstrap.com/), [React Bootstrap](https://react-bootstrap.github.io/)
- **HTTP Client:** [Axios](https://axios-http.com/)
- **Routing:** [React Router DOM](https://reactrouter.com/) (v6)
- **Notifications:** [React Toastify](https://fkhadra.github.io/react-toastify/)

### Backend
- **Framework:** [Spring Boot](https://spring.io/projects/spring-boot) (v3.2.3)
- **Language:** Java 17
- **Security:** Spring Security & JWT (JSON Web Tokens)
- **Database Search:** Spring Data JPA
- **Database:** PostgreSQL
- **PDF Generation:** OpenPDF

---

## 📂 Project Structure

The project is divided into two main applications:

*   **`car-rental-system-backend`**: RESTful API server.
*   **`car-rental-system-frontend`**: React single-page application.

---

## 🏁 Getting Started

Follow these instructions to set up the project locally.

### Prerequisites
- **Node.js** and **npm** installed.
- **Java Development Kit (JDK) 17** or later.
- **Maven** installed.
- **PostgreSQL** installed and running.

---

### 🔙 Backend Setup

1.  **Navigate to the backend directory:**
    ```bash
    cd car-rental-system-backend
    ```

2.  **Configure Database:**
    *   Create a PostgreSQL database (e.g., `car_rental_db`).
    *   Open `src/main/resources/application.properties` (or `application.yml`) and update the database credentials:
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/car_rental_db
        spring.datasource.username=your_username
        spring.datasource.password=your_password
        ```

3.  **Run the Application:**
    You can run it using the Maven wrapper or your IDE.
    ```bash
    ./mvnw spring-boot:run
    ```
    The backend server will start on `http://localhost:8080`.

---

### 🖥️ Frontend Setup

1.  **Navigate to the frontend directory:**
    ```bash
    cd car-rental-system-frontend
    ```

2.  **Install Dependencies:**
    ```bash
    npm install
    ```

3.  **Configure API Endpoint (Optional):**
    If your backend runs on a different port, check the Axios configuration (usually in a `service` folder or environment file) to ensure it points to the correct backend URL.

4.  **Start the Development Server:**
    ```bash
    npm start
    ```
    The application will open in your browser at `http://localhost:3000`.

---

## 📸 Screenshots

| Home Page | Car Details |
|-----------|-------------|
| ![Home](https://placehold.co/600x400?text=Home+Page) | ![Detail](https://placehold.co/600x400?text=Car+Details) |

| Admin Dashboard | Booking Flow |
|-----------------|--------------|
| ![Admin](https://placehold.co/600x400?text=Admin+Dashboard) | ![Booking](https://placehold.co/600x400?text=Booking+Flow) |

*(Replace these placeholders with actual screenshots from your `license-image` or `variant-image` folders)*

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:
1.  Fork the repository.
2.  Create a feature branch (`git checkout -b feature/NewFeature`).
3.  Commit your changes (`git commit -m 'Add NewFeature'`).
4.  Push to the branch (`git push origin feature/NewFeature`).
5.  Open a Pull Request.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
