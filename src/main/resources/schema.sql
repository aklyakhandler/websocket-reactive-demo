CREATE TABLE user (
id IDENTITY PRIMARY KEY
);

CREATE TABLE stock (
id IDENTITY PRIMARY KEY,
name VARCHAR (32),
price DECIMAL
);

CREATE TABLE contract (
id IDENTITY PRIMARY KEY,
user_id INT,
stock_id INT,
stock_amount INT,

FOREIGN KEY (user_id) REFERENCES user(id),
FOREIGN KEY (stock_id) REFERENCES stock(id)
);