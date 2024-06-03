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
VALUES (100000, '2024-06-01 08:00', 'USER - Breakfast', 400),
       (100000, '2024-06-01 10:00', 'USER - Second Breakfast', 150),
       (100000, '2024-06-01 12:30', 'USER - Lunch', 600),
       (100000, '2024-06-01 16:30', 'USER - Afternoon snack', 100),
       (100000, '2024-06-01 18:30', 'USER - Dinner', 600),
       (100000, '2024-06-01 21:30', 'USER - Late night snack', 100),

       (100001, '2024-06-01 08:15', 'ADMIN - Breakfast', 500),
       (100001, '2024-06-01 10:30', 'ADMIN - Second Breakfast', 200),
       (100001, '2024-06-01 13:30', 'ADMIN - Lunch', 600),
       (100001, '2024-06-01 16:30', 'ADMIN - Afternoon snack', 300),
       (100001, '2024-06-01 19:30', 'ADMIN - Dinner', 600),
       (100001, '2024-06-01 22:30', 'ADMIN - Late night snack', 300);


