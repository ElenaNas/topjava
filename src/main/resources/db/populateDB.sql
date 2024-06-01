DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals(user_id,date_time, description, calories)
VALUES (100000, '2024-06-01 08:00', 'Breakfast', 400),
       (100000, '2024-06-01 12:30', 'Lunch', 600),
       (100000, '2024-06-01 18:30', 'Dinner', 600);


