# Todo Application

This is a simple Todo Application built with Spring Boot.

## Getting Started

## 1.  Clone the project:
   ```
   git clone https://github.com/savindu29/Todo-application-backend-with-spring.git
```
  

##  2. Setup Database Credentials:

 Go to src/main/resources and open application.properties.
 Update the database URL:
```
spring.datasource.url=jdbc:mysql://localhost:3306/todo_app?createDatabaseIfNotExist=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## 3. Insert data into the database

you can use the below query to insert data into the master tables
```
USE todo_app;
INSERT INTO priority (id, name, code) VALUES
(1, 'Urgent', '1'),
(2, 'High', '2'),
(3, 'Normal', '3'),
(4, 'Low', '4');
```

## 3. API Documentation

Access the Swagger Open API documentation at: 
```
http://localhost:8080/swagger-ui/index.html#/
```


