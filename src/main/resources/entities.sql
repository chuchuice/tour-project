CREATE TABLE Human (
                       id int8 GENERATED ALWAYS AS IDENTITY ( START 1 INCREMENT 1) PRIMARY KEY,
                       role VARCHAR NOT NULL,
                       name VARCHAR(32) NOT NULL,
                       login VARCHAR NOT NULL,
                       password VARCHAR NOT NULL
);

INSERT INTO Human(role, name, login, password) VALUES ('ADMIN', 'Pavel', 'pavel', '$2a$10$agcXVxZnhAZywVjC9qM18OQFlH.8CheG3lKI7toNWraaLhaTpXdX6');