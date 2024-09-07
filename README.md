# Book House - Microservices-based Online Bookstore

<p align="center"> 
  <img src="https://github.com/user-attachments/assets/939a339b-abeb-4650-9eaa-a8f7f4af16ee" alt="Book House Logo" width="400"/> 
</p> 

<div align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Microservices-4285F4?style=for-the-badge&logo=google-cloud&logoColor=white" alt="Microservices" />
  <img src="https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white" alt="MongoDB" />
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL" />
  <img src="https://img.shields.io/badge/Google_Cloud-4285F4?style=for-the-badge&logo=google-cloud&logoColor=white" alt="Google Cloud" />
  <img src="https://img.shields.io/badge/MVC-1572B6?style=for-the-badge&logo=mvc&logoColor=white" alt="MVC" />
  <img src="https://img.shields.io/badge/API-02569B?style=for-the-badge&logo=api&logoColor=white" alt="REST API" />
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  <img src="https://img.shields.io/badge/Docker_Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker Compose" />
  <img src="https://img.shields.io/badge/Eureka_Server-4CAF50?style=for-the-badge&logo=spring&logoColor=white" alt="Eureka Server" />
  <img src="https://img.shields.io/badge/Mail_Service-SendGrid-4285F4?style=for-the-badge&logo=sendgrid&logoColor=white" alt="SendGrid" />
</div>

---

**Book House** is an online bookstore project designed to provide a comprehensive experience for both users and administrators. This application is built on a microservices architecture, enabling high scalability and flexibility. **Book House** is developed using technologies such as **Java**, **Spring Boot**, **MongoDB**, **MySQL**, **Spring Cloud**, and others, with implementation and deployment managed through **Docker** and **Docker Compose**.

The application consists of several key microservices that manage different areas of the system. **CatalogueService** handles the book catalog in a MongoDB database, while **CartService** manages shopping carts, orders, and invoices in MySQL. **AuthService** manages user authentication and operates on MongoDB as well. **ReviewService** deals with book reviews and ratings, and **NotificationService** handles transactional emails and invoice generation using JasperReports and SendGrid.

Some of the standout features of **Book House** include an intuitive user interface that allows customers to explore the book catalog, apply advanced filters, and make purchases effortlessly. Administrators can manage users and view purchase histories through a robust admin panel. Additionally, the app ensures a secure experience with JWT-based authentication and efficient notifications through automatically generated invoices and emails.

## Table of Contents

- [Features](#features)
- [Application](#application)
- [Tools Used](#tools-used)
- [Installation](#installation)
- [Areas for Improvement](#areas-for-improvement)
- [Contributors](#contributors)
- [License](#license)
- [Contact me](#contact-me)

## Features

- **Distributed Microservices**: The application is designed with a microservices architecture, including services like **CatalogueService** for the book catalog, **CartService** for carts and orders, **AuthService** for user authentication, **ReviewService** for reviews and ratings, and **NotificationService** for emails and invoice generation.

- **Hybrid Database**: Utilizes **MongoDB** for non-relational data management and **MySQL** for relational data, ensuring efficient information handling tailored to each service's needs.

- **Intuitive User Interface**: Provides a seamless experience with a modern interface that makes book navigation, advanced filtering, and purchases simple. Includes an admin panel for user management and monitoring purchases.

- **PayPal Integration**: Enables users to securely complete payments via **PayPal**, simplifying the purchase process on the platform.

- **Load Balancing**: Uses **Docker** and **Docker Compose** to create at least two instances of each service, ensuring high availability and efficient load balancing to handle traffic.

- **Auto-discovery Server**: Implements **Netflix Eureka** as a discovery server, allowing microservices to dynamically register and discover each other.

- **Centralized Configuration Server**: Utilizes **Spring Cloud Config Server** for centralized configuration, with configuration files stored in a **GitHub** repository, ensuring consistent management and updates across all microservices.

- **Spring Cloud Gateway**: The **API Gateway** powered by **Spring Cloud** acts as the single entry point for clients, routing requests to the appropriate services and managing communication between different microservices.

- **Secure Authentication**: Implements **Spring Security** with JWT to ensure secure authentication and authorization, protecting access to application features and ensuring only authorized users can perform certain actions.

- **Monitoring and Scalability**: Utilizes **Zipkin** for transaction monitoring and **Docker** for deployment, ensuring the application is scalable and can efficiently handle traffic and transactions.

## Application

**Book House** offers a series of views designed to provide a smooth and comprehensive user experience. Below are the detailed views available in the application:

### Instances with Eureka Server

The application uses **Netflix Eureka** for service management. The Eureka Server dashboard allows you to visualize the status of all deployed service instances, facilitating the administration and monitoring of the microservices infrastructure.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/1.png" alt="Instances with Eureka Server" width="800">
</div>
<br>

The architecture, as previously observed, is based on microservices with high availability. To ensure high availability, each microservice has at least two instances running. This guarantees that if one instance fails, another is available to handle the requests, thereby minimizing downtime. The number of instances is managed in Docker Compose using the "Deploy" parameter. Additionally, an API Gateway has been effectively implemented to route all access URLs to the microservices, facilitating communication between the system's components. In the test image, only one instance per service is visible because Docker is not being used at that time.

---

### Login and Registration View

Users can access the application via a login and registration page. New users complete a registration form, while existing users enter their credentials in the login form.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/2.png" alt="Login" width="800">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/3.png" alt="Registration" width="800">
</div>
<br>

---

### Registration Email

After completing the registration, users receive a confirmation email. Below are the desktop and mobile versions of the email.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/4.png" alt="Email Mobile" width="200">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/5.png" alt="Email Desktop" width="400">
</div>

---

### Home View

The main page of the application presents an attractive design with book recommendations, new releases, and links to the main sections. Users can explore the featured content and access key areas of the store.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/6.png" alt="Home 1" width="800">
  <br>
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/7.png" alt="Home 2" width="800">
  <br>
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/8.png" alt="Home 3" width="800">
  <br>
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/9.png" alt="Home 4" width="800">
  <br>
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/10.png" alt="Home 5" width="800">
</div>
<br>

We decided to create a comprehensive view for Book House with a highly intuitive and aesthetic interface. In this main view, users will have access to book recommendations, see the latest books added, and more. They will also have the option to explore the full catalog. If the user is logged in, they will be able to make purchases, view their purchase history, and access additional features. Furthermore, if the user is logged in and has admin privileges, they will have access to an admin panel with a dashboard providing information about the state of the bookstore.

---

### Catalog View

Displays a list of books available in the library, with options for applying filters and sorting. Users can search for books by different criteria.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/11.png" alt="Catalog" width="800">
</div>
<br>

It is essential that the book catalog allows filtering by author, title, and literary genre. Below is an example of these filters in action. The filters are sensitive in the sense that when searching for "Sleep," for example, it will find all books containing that word or consecutive letters in the title, and the same applies to the author, enriching the search and improving SEO. Additionally, books can have more than one genre, but only the main genre is displayed on the cover. Therefore, if you filter by a genre and see a book with a cover showing a different genre than the one you selected, it means that one of its secondary genres belongs to the selected category.

<br>

<div align="center">
  <h3>Filtering by Author (Sophie Swayn):</h3>
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/12.png" alt="Filter by Author" width="800">
</div>
<br>

<div align="center">
  <h3>Filtering by Title (Sleep): </h3>
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/13.png" alt="Filter by Title" width="800">
</div>
<br>
  
<br>
<div align="center">
  <h3>Filtering by Genre (Humor): </h3>
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/14.png" alt="Filter by Genre" width="800">
</div>
<br>
  
As observed, there are also some additional filters, including:

- Sort from A to Z
- Sort from Z to A
- Sort from lowest price to highest price
- Sort from highest price to lowest price

---

### Book Detail View

Provides complete information about a book, including its description, author, genre, user reviews, and related books. Users can explore the details and opinions of other readers.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/15.png" alt="Book Detail" width="800">
  <br>
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/16.png" alt="Additional Book Details" width="800">
</div>
<br>


- **Review Restriction**: A user who hasn't purchased the book cannot leave a review.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/17.png" alt="Review Restriction" width="800">
</div>

---

### Review List View

Displays all the reviews of a specific book. If the book has no reviews, a message is shown indicating that the book has not been rated yet.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/18.png" alt="Review List" width="800">
</div>
<br>

The average rating value is calculated based on the list of reviews that the book in question has.

---

### Shopping Cart View

Shows the items added to the cart, allowing users to review their selections before proceeding to checkout.

<br>

<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/19.png" alt="Shopping Cart" width="800">
</div>

---

### Checkout View (Payment Confirmation)

Allows users to enter payment details and complete the purchase. The system uses **PayPal** for secure payment processing.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/20.png" alt="Checkout" width="800">
</div>
<br>


- **PayPal Payment Confirmation View**: Displays the payment confirmation after completing the transaction.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/21.png" alt="Payment Confirmation" width="400">
  <img src="https://github.com/user-attachments/assets/acd67e98-3b88-4af4-b237-02f8195b49de" alt="Payment Confirmation" width="400">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/22.png" alt="PayPal Confirmation View" width="800">
</div>

---

### Order Confirmation Email

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/23.png" alt="Order Confirmation Email" width="200">
</div>

---

### Invoice Design in Jasper Report Studio

The invoice is generated using Jasper Reports.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/24.png" alt="Invoice Design in Jasper Report Studio" width="600">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/25.png" alt="Generated PDF Invoice" width="600">
</div>
<br>

After this, the `.jrxml` file was exported to the notification service, and a `ReportService` was created where the confirmed order data is sent and loaded into the invoice file. Finally, the report is exported as a PDF and attached to the email sent via the SendGrid service.

---

### User Creation View

Allows administrators to create new users with different roles and permissions.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/26.png" alt="User Creation" width="800">
</div>

---

### Purchase History View

Shows a summary of all previous purchases made by the user.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/27.png" alt="Purchase History" width="800">
</div>

---

### My Purchases View

Allows users to view a list of all their purchases, with details for each transaction.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/28.png" alt="My Purchases" width="800">
</div>

---

### Admin Dashboard View

We created a dashboard that displays purchases made by day and by genre. We also included a purchase history and, additionally, added statistical cards showing the total number of orders, the number of genres, the number of books, and the number of users in the system.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/29.png" alt="Admin Dashboard" width="800">
</div>

---

### Monitoring with Zipkin

The application uses **Zipkin** for microservice monitoring. Although embedded versions of Zipkin are deprecated, it has been implemented as a Docker image, integrated with Docker Compose. This allows tracking and monitoring the performance of microservices, excluding the discovery server.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/30.png" alt="Monitoring with Zipkin" width="800">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/31.png" alt="Zipkin" width="800">
</div>

---

### Security and Authentication

The system has high performance in terms of security. Upon logging in, the user receives a token valid for one hour. This token is encrypted and stored in a cookie, which the client uses to make requests to the Gateway. The Gateway, in collaboration with the authentication service, validates the token and either grants or denies access to the route. Additionally, the user's role is checked to ensure it has the necessary permissions for access. Otherwise, the system will generate an error.

<br>
<div align="center">
  <img src="https://github.com/darvybm/library-microservices/blob/main/test-images/32.png" alt="Security and Authentication" width="800">
</div>
<br>

It is important to note that when a session expires or when logging out, the user must log in again to refresh their token.

## **Tools Used**

- ![Java](https://img.shields.io/badge/Java-007396?logo=java&logoColor=white&style=flat-square) **Java**: The primary programming language used for backend development of the platform.

- ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?logo=spring-boot&logoColor=white&style=flat-square) **Spring Boot**: The framework used to build the backend of the application, providing a robust and scalable architecture.

- ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?logo=spring-security&logoColor=white&style=flat-square) **Spring Security**: Implemented for authentication and authorization within the platform, ensuring secure access to functionalities.

- ![JWT](https://img.shields.io/badge/JWT-000000?logo=json-web-tokens&logoColor=white&style=flat-square) **JWT (JSON Web Tokens)**: Used for managing authentication and secure communication between the web application and microservices.

- ![MongoDB](https://img.shields.io/badge/MongoDB-47A248?logo=mongodb&logoColor=white&style=flat-square) **MongoDB**: NoSQL database used for storing various types of data across different services.

- ![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white&style=flat-square) **MySQL**: Relational database used for managing structured data related to orders, reviews, and other transactional information.

- ![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-6DB33F?logo=spring-cloud&logoColor=white&style=flat-square) **Spring Cloud**: Provides tools for managing distributed systems, including configuration management, service discovery, and circuit breakers.

- ![Spring Cloud Gateway](https://img.shields.io/badge/Spring_Cloud_Gateway-6DB33F?logo=spring-cloud&logoColor=white&style=flat-square) **Spring Cloud Gateway**: Used as an API Gateway to route requests to various microservices.

- ![Feign](https://img.shields.io/badge/Feign-00BFFF?logo=feign&logoColor=white&style=flat-square) **Feign Client**: Used for declarative REST client creation, simplifying communication between microservices.

- ![Zipkin](https://img.shields.io/badge/Zipkin-FF4081?logo=zipkin&logoColor=white&style=flat-square) **Zipkin**: Distributed tracing system used for monitoring and troubleshooting the performance of microservices.

- ![JasperReports](https://img.shields.io/badge/JasperReports-4B0082?logo=jasperreports&logoColor=white&style=flat-square) **JasperReports**: Reporting tool used for generating and formatting invoices and other documents.

- ![DataFaker](https://img.shields.io/badge/DataFaker-6DB33F?logo=datafaker&logoColor=white&style=flat-square) **DataFaker**: Library used for generating realistic test data, such as random books and user profiles.

- ![HTML5](https://img.shields.io/badge/HTML5-E34F26?logo=html5&logoColor=white&style=flat-square) ![CSS3](https://img.shields.io/badge/CSS3-1572B6?logo=css3&logoColor=white&style=flat-square) ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?logo=javascript&logoColor=black&style=flat-square) ![Bootstrap](https://img.shields.io/badge/Bootstrap-7952B3?logo=bootstrap&logoColor=white&style=flat-square) **HTML, CSS, JS, Bootstrap**: Front-end technologies used to develop the user interface, ensuring a responsive and interactive experience.

- ![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white&style=flat-square) ![Docker Compose](https://img.shields.io/badge/Docker_Compose-2496ED?logo=docker&logoColor=white&style=flat-square) **Docker and Docker Compose**: Used to containerize and orchestrate the microservices, simplifying deployment and scaling.

## Installation

### Prerequisites

- **Java 17** or higher
- **Gradle** (instead of Maven)
- **Docker** and **Docker Compose**
- Configured databases (MongoDB and MySQL)

### Steps

1. **Clone the repository**:

   ```bash
   git clone https://github.com/user/book-house.git
   ```

2. **Navigate to the project directory**:

   ```bash
   cd book-house
   ```

3. **Configure databases**:
   
   - Deploy MongoDB either in the cloud or locally (e.g., MongoDB Atlas).
   - Deploy MySQL either in the cloud or locally.

4. **Configure local properties**:

   - **AuthService**: Configure the JWT secret in the `application.properties` file.

     ```properties
     security.jwt.secret-key=addjwtkey
     ```

   - **CatalogueService**: Configure whether you want to generate random books on application startup in the `application.properties` file.

     ```properties
     library.catalogue.generate-books-on-startup=false
     library.catalogue.generate-books-count=50
     ```

   - **ClientService**: Configure local properties such as the port, encryption secret, and PayPal configuration in the `application.properties` file.

     ```properties
     crypto.secret-key=${CRYPTO_SECRET_KEY:addKey}
     paypal.client-id=${PAYPAL_CLIENT_ID:addClientId}
     paypal.client-secret=${PAYPAL_CLIENT_SECRET:addClientSecret}
     paypal.mode=${PAYPAL_MODE:sandbox}
     ```

   - **ConfigServer**: Configure access to your Git repository in the `application.properties` file.

     ```properties
     spring.cloud.config.server.git.uri=add your git repo here with the folder in url
     spring.cloud.config.server.git.clone-on-start=true
     spring.cloud.config.server.git.username=add your git username here
     spring.cloud.config.server.git.password=add your git password or token here
     spring.cloud.config.server.git.default-label=master
     ```
> [!WARNING]  
> Reference should be made to the folder where the configuration files are located, not to the repository in general.

   - **GatewayService**: Configure the JWT secret in the `application.properties` file.

     ```properties
     security.jwt.secret-key=addjwtkey
     ```

   - **NotificationService**: Configure SendGrid in the `application.properties` file.

     ```properties
     sendgrid.api.key=add_your_sendgrid_api_key_here
     sendgrid.api.sender-mail=example@gmail.com
     ```

   - Modify in the Auth, Catalogue and Notification services the following url of MongoDB (this is in the properties of the configuration server).

     ```properties
     spring.data.mongodb.uri=${SPRING_DATA_MONGODB_URI:add_your_mongodb_uri_here}
     ```

   - Modify in the Cart and Review services the following MySQL configuration (this is in the properties of the configuration server).

     ```properties
     spring.datasource.url=${SPRING_DATASOURCE_URL:add_your_mysql_url_here}
     spring.datasource.username=${SPRING_DATASOURCE_USERNAME:add_your_mysql_username_here}
     spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:add_your_mysql_password_here}
     ```

5. **Run the services**:

   - **Configure and run with Docker Compose**:
   
     Create a `.env` file in the same directory as `docker-compose.yml` with the following variables:

     ```dotenv
     # Eureka configuration
     EUREKA_PORT=8761

     # Config Server configuration
     CONFIG_SERVER_PORT=8889

     # Database configuration
     MONGO_URI=mongodb+srv://libraryUser:your_password@librarycluster.mongodb.net/
     MONGO_AUTH_DB=auth_db
     MONGO_CATALOGUE_DB=catalogue_db
     MONGO_NOTIFICATION_DB=notification_db

     MYSQL_URL=jdbc:mysql://your_mysql_host:3306/
     MYSQL_USER=root
     MYSQL_PASSWORD=your_mysql_password
     MYSQL_SHOPPING_CART_DB=shopping_cart
     MYSQL_REVIEW_DB=review_db

     # Gateway configuration
     GATEWAY_PORT=8090

     # Client configuration
     CLIENT_PORT=8081

     # Client configuration

     CRYPTO_SECRET_KEY=crypto_secret_key

     PAYPAL_CLIENT_ID=key
     PAYPAL_CLIENT_SECRET=secret
     ```

     Then, run the services with Docker Compose:

     ```bash
     docker-compose up --build
     ```

   - **Without Docker Compose**:

     If not using Docker Compose, follow this order to run the projects:

     1. **DiscoveryServer** (Service discovery)
     2. **ConfigServer** (Centralized configuration server)
     3. **AuthService**
     4. **CatalogueService**
     5. **CartService**
     6. **ReviewService**
     7. **NotificationService**
     8. **GatewayService** (API Gateway)
     9. **ClientService** (User interface)

6. **Access the application**:

   Open your browser and visit `http://localhost:8081` to access the user interface.
   

## Areas for Improvement

- **Performance Enhancement in Recommendation Algorithm**: There is no recommendation algorithm; it was only simulated.

- **Expand Administration Panel Functionality**: Add additional features to the admin panel for more comprehensive and detailed management.

- **Add Unit and Integration Tests for Greater Code Coverage**: Implement and improve tests to ensure code stability and detect issues early.

- **Pagination in the Backend**: Implement pagination to handle large volumes of data more efficiently and improve page load speed.

- **Documentation and Code Organization**: Add explanatory comments and improve code organization to facilitate maintenance and understanding.

## Contributors
Here are the contributors to this project:

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/darvybm">
        <img src="https://github.com/darvybm.png" width="100px;" alt="darvybm"/>
        <br />
        <sub><b>darvybm</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/AnthonyBeato">
        <img src="https://github.com/AnthonyBeato.png" width="100px;" alt="AnthonyBeato"/>
        <br />
        <sub><b>AnthonyBeato</b></sub>
      </a>
    </td>
  </tr>
</table>

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact Me

<p align="center">
  <a href="https://www.linkedin.com/in/darvybm" target="_blank">
    <img src="https://img.shields.io/badge/LinkedIn-@darvybm-blue?style=flat&logo=linkedin&logoColor=white" alt="LinkedIn Badge"/>
  </a>
  <a href="mailto:darvybm@gmail.com" target="_blank">
    <img src="https://img.shields.io/badge/Email-Contact%20Me-orange" alt="Email Badge"/>
  </a>
</p>
