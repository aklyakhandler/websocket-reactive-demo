INSERT INTO stock (name, price)
VALUES ('Дорогая акция', 1500),
       ('Средняя акция', 100),
       ('Дешевая акция', 10);

INSERT INTO user (id)
VALUES (1);

INSERT INTO contract (user_id, stock_id, stock_amount)
VALUES (1, 1, 10),
       (1, 2, 30),
       (1, 3, 200);