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
       (100000, '2024-06-01 10:00', 'Second Breakfast', 150),
       (100000, '2024-06-01 12:30', 'Lunch', 600),
       (100000, '2024-06-01 16:30', 'Afternoon snack', 100),
       (100000, '2024-06-01 18:30', 'Dinner', 600),
       (100000, '2024-06-01 21:30', 'Late night snack', 100),

       (100001, '2024-06-01 08:15', 'Breakfast', 500),
       (100001, '2024-06-01 10:30', 'Second Breakfast', 200),
       (100001, '2024-06-01 13:30', 'Lunch', 600),
       (100001, '2024-06-01 16:30', 'Afternoon snack', 300),
       (100001, '2024-06-01 19:30', 'Dinner', 600),
       (100001, '2024-06-01 22:30', 'Late night snack', 300);


