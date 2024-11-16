Download zip or clone the application repo.
After starting the servr run these following 3 commands in the MySql database to be able to work on the functoinalities:
USE book_reservation_db;
INSERT INTO roles(role_name) VALUES('USER');
INSERT INTO roles(role_name) VALUES('ADMIN');

To open the Swagger file, type this url in your browser: http://localhost:8080/swagger-ui/index.html
PS: The backend server is running in the 8080 port.
