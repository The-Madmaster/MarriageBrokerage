# MarriageBrokerage
# Mahi Marriage Brokerage System

A modular, broker-centric marriage bureau web application built with Java Spring Boot backend, React TypeScript frontend, SQL Server database, and JWT authentication.

## Features

- **Multi-Role System**: Admin, Broker, and Client roles with appropriate permissions
- **Secure Authentication**: JWT-based authentication with protected routes
- **Modern UI**: React TypeScript frontend with Material-UI components
- **RESTful APIs**: DTO-based backend APIs for clean data transfer
- **Database Support**: SQL Server for production, H2 for development
- **Modular Architecture**: Scalable and maintainable codebase

## Tech Stack

### Backend
- Java 17
- Spring Boot 3.1.4
- Spring Security (JWT)
- Spring Data JPA
- SQL Server / H2 Database
- Maven

### Frontend
- React 19 with TypeScript
- Material-UI (MUI)
- React Router
- Axios for HTTP client
- React Query for state management

## Getting Started

### Prerequisites
- Java 17+
- Node.js 16+
- Maven
- SQL Server (optional - H2 works for development)

### Installation

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd MarriageBrokerage/Project
   ```

2. **Backend Setup**
   ```bash
   # Build the Spring Boot application
   mvn clean install
   ```

3. **Frontend Setup**
   ```bash
   # Install frontend dependencies
   cd frontend
   npm install --legacy-peer-deps
   cd ..
   ```

### Running the Application

#### Option 1: Run Both Together (Recommended)
Install concurrently if not already installed:
```bash
npm install concurrently
```

Then run both frontend and backend:
```bash
npm run dev
```

#### Option 2: Run Separately

**Backend** (in root directory):
```bash
mvn spring-boot:run
```
Backend will start at `http://localhost:8080`

**Frontend** (in separate terminal):
```bash
cd frontend
npm start
```
Frontend will start at `http://localhost:3000`

### Default Access

The application uses H2 in-memory database by default. To switch to SQL Server:

1. Update `src/main/resources/application.properties`
2. Uncomment SQL Server configuration
3. Comment out H2 configuration

### API Endpoints

#### Authentication
- `POST /api/auth/login` – User login
- `POST /api/auth/register` – User registration

#### Broker Operations
- `GET /api/broker/clients` – Get broker's clients
- `POST /api/broker/clients` – Add new client

#### Admin Operations
- `GET /api/admin/brokers` – Get all brokers

### Frontend Routes

- `/login` – User login
- `/register` – User registration
- `/dashboard` – Role-based dashboard
- `/clients` – Client management (Broker/Admin)
- `/brokers` – Broker management (Admin only)
- `/profile` – User profile

### User Roles & Permissions

#### Admin
- Manage all brokers
- View system analytics
- Full system access

#### Broker
- Manage assigned clients
- Add new clients
- View client matches

#### Client
- View profile
- Browse matches
- Manage preferences

## Development

### Project Structure

```
Project/
├── src/main/java/com/mahi/marriagebrokerage/
│   ├── controller/          # REST Controllers
│   ├── dto/                # Data Transfer Objects
│   ├── entity/             # JPA Entities
│   ├── repository/         # Data Repositories
│   ├── security/           # Security Configuration
│   └── service/            # Business Logic
├── frontend/
│   ├── src/
│   │   ├── components/     # React Components
│   │   ├── contexts/       # React Contexts
│   │   ├── pages/          # Page Components
│   │   ├── services/       # API Services
│   │   └── types/          # TypeScript Types
│   └── public/
└── README.md
```

### Adding New Features

1. **Backend**: Add entities, repositories, services, and controllers
2. **Frontend**: Create components, services, and routes
3. **Update types**: Maintain TypeScript interfaces for type safety

## Configuration

### Database Configuration
Edit `src/main/resources/application.properties`:

```properties
# For SQL Server
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=marriage_brokerage
spring.datasource.username=sa
spring.datasource.password=YourPassword123

# For H2 (Development)
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
```

### JWT Configuration
```properties
jwt.secret=YourSecretKey
jwt.expiration=86400000
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

---

**Built with ❤️ for modern marriage brokerage management**
