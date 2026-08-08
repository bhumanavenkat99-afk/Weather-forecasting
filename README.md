# Weather Forecasting Using Decision Tree and Regression Algorithms

## Project Overview
A Java Spring Boot application for weather forecasting using historical weather data. The system supports user registration, file upload, model training, Decision Tree classification for rain prediction, regression for next-day temperature prediction, and prediction history tracking.

## Features
- User registration and login
- Secure password hashing with BCrypt
- Upload historical weather data via CSV
- Store weather and prediction history in MySQL
- Decision Tree classification for Rain / No Rain
- Regression prediction for next-day temperature
- Dashboard with charts and performance metrics
- REST APIs for authentication, weather upload, predictions, and model performance

## Technologies Used
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- Thymeleaf
- MySQL
- Maven
- Weka
- Bootstrap
- Chart.js
- JUnit 5 and Mockito

## System Architecture
The application follows a clean layered architecture:
- Controller layer handles REST and page requests
- Service layer contains business logic and ML model management
- Repository layer manages database interaction
- Model layer defines JPA entities
- DTO layer structures API request and response payloads
- ML layer builds and evaluates classifiers and regressors

## Dataset Description
The `dataset/weather_data.csv` file contains historical weather values with fields:
- date
- temperature
- humidity
- pressure
- windSpeed
- cloudCover
- precipitation
- weatherCondition

Place your CSV file in the dataset folder or upload it from the frontend.

## Database Design
Create a MySQL database named `weather_forecasting`.

### Tables
- `users`
- `weather_data`
- `prediction_history`

## API Documentation
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login user
- `POST /api/weather/upload` - Upload CSV weather data
- `GET /api/weather/history` - Retrieve weather records
- `POST /api/prediction/decision-tree` - Decision Tree prediction
- `POST /api/prediction/regression` - Regression prediction
- `POST /api/prediction/predict` - Combined prediction
- `GET /api/prediction/history` - User prediction history
- `GET /api/model/performance` - Model performance metrics

## Project Structure
```
weather-forecasting/
├── src/main/java/com/weatherforecast/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   ├── dto/
│   ├── ml/
│   └── config/
├── src/main/resources/
│   ├── templates/
│   └── static/
├── dataset/
│   └── weather_data.csv
├── pom.xml
└── README.md
```

## Installation Instructions
1. Clone or download this repository.
2. Install Java 17 and MySQL.
3. Create the database:
```sql
CREATE DATABASE weather_forecasting;
```
4. Update `src/main/resources/application.properties` with your MySQL username and password.
5. Place the sample dataset file inside the `dataset` folder or upload your own CSV file using the frontend.

## How to Run
From the project root:
```bash
mvn clean install
mvn spring-boot:run
```

The application will run on `http://localhost:8080`.

## How to Test APIs
Use Postman or another API client to call the endpoints listed in the API documentation.

## Future Enhancements
- Add JWT authentication for API security
- Support more weather categories and multi-class predictions
- Add user roles and admin dashboard
- Enable file preview before upload
- Add advanced metrics such as AUC and cross-validation
- Implement dataset feature selection and normalization

## Notes
- The frontend is built with Bootstrap and Thymeleaf.
- The machine learning pipeline uses Weka for training and evaluation.
- Ensure MySQL is running and the database exists before starting the application.
