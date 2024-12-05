CREATE TABLE Human (
                       id int8 GENERATED ALWAYS AS IDENTITY ( START 1 INCREMENT 1) PRIMARY KEY,
                       role VARCHAR NOT NULL,
                       name VARCHAR(32) NOT NULL,
                       surname VARCHAR(32) NOT NULL,
                       patronymic VARCHAR(32) NOT NULL,
                       login VARCHAR NOT NULL,
                       password VARCHAR NOT NULL,
                       email VARCHAR NOT NULL,
);

INSERT INTO Human(role, name, login, password) VALUES ('ADMIN', 'Pavel', 'pavel', '$2a$10$agcXVxZnhAZywVjC9qM18OQFlH.8CheG3lKI7toNWraaLhaTpXdX6');

CREATE TABLE Hotels (
                        id INT8 GENERATED ALWAYS AS IDENTITY ( START 1 INCREMENT 1) PRIMARY KEY,
                        title VARCHAR NOT NULL,
                        stars INT CHECK ( stars > 0 AND stars < 6 ),
                        country VARCHAR NOT NULL,
                        city VARCHAR NOT NULL,
                        address VARCHAR NOT NULL
);

CREATE TABLE Rooms (
                       id INT8 GENERATED ALWAYS AS IDENTITY ( START 1 INCREMENT 1) PRIMARY KEY,
                       number INT4 NOT NULL,
                       count_of_beds INT4 NOT NULL,
                       cost INT4 NOT NULL,
                       lodger_id INT8 REFERENCES human(id),
                       hotel_id INT8 REFERENCES Hotels(id),
                       status VARCHAR NOT NULL
);

INSERT INTO hotels(title, stars, country, city, address)
VALUES ('Элеон', 5, 'Россия', 'Москва', 'ул. Большая Якиманка, д.1');

INSERT INTO rooms(number, count_of_beds, lodger_id, hotel_id, cost) VALUES (100, 2, null, 1, 5000);
INSERT INTO rooms(number, count_of_beds, lodger_id, hotel_id, cost) VALUES (101, 2, null, 1, 5000);
INSERT INTO rooms(number, count_of_beds, lodger_id, hotel_id, cost) VALUES (102, 2, null, 1, 5000);
INSERT INTO rooms(number, count_of_beds, lodger_id, hotel_id, cost) VALUES (300, 2, null, 1, 5000);
INSERT INTO rooms(number, count_of_beds, lodger_id, hotel_id, cost) VALUES (301, 3, null, 1, 7000);
INSERT INTO rooms(number, count_of_beds, lodger_id, hotel_id, cost) VALUES (302, 4, null, 1, 9000);
INSERT INTO rooms(number, count_of_beds, lodger_id, hotel_id, cost) VALUES (500, 4, null, 1, 9000);
INSERT INTO rooms(number, count_of_beds, lodger_id, hotel_id, cost) VALUES (501, 4, null, 1, 9000);
INSERT INTO rooms(number, count_of_beds, lodger_id, hotel_id, cost) VALUES (502, 8, null, 1, 15000);

CREATE TABLE room_availability (
                                   id INT8 NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT 1 START 1) PRIMARY KEY,
                                   room_id INT8 NOT NULL REFERENCES rooms(id),
                                   available_date DATE NOT NULL
);