INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Allowance', 'INCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Reward', 'INCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Common cost', 'OUTCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Overhead', 'OUTCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Internet', 'OUTCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Tax', 'OUTCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Insurance', 'OUTCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Petrol', 'OUTCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Restaurant', 'OUTCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Vitamins', 'OUTCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Private lesson', 'OUTCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Cinema', 'OUTCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Computer', 'OUTCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Hobby', 'OUTCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Fitness season ticket', 'OUTCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'Clothes', 'OUTCOME', 1);

INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'Salary', 'INCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'Moon lighting', 'INCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'Gift', 'INCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'Lodging', 'OUTCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'Car', 'OUTCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'Food', 'OUTCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'Beatuy care', 'OUTCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'Health care', 'OUTCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'Education', 'OUTCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'Entertainment', 'OUTCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'Sport', 'OUTCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'Clothes', 'OUTCOME', 1);

INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (1, 1); -- Salary
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (1, 2);
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (4, 3); -- Lodging
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (4, 4);
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (4, 5);
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (4, 6);
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (5, 6); -- Car
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (5, 7);
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (5, 8);
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (6, 9); -- Food
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (8, 10); -- Health care
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (9, 11); -- Education
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (10, 12); -- Entertainment
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (10, 13);
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (10, 14);
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (11, 15); -- Sport
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (11, 16);


INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Salary', 9999, 'EUR', CURRENT_DATE - 150, NULL, CURRENT_DATE - 15, 1, 1, NULL, 1, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Fringe benefit', 10000, 'HUF', CURRENT_DATE - 150, NULL, CURRENT_DATE - 15, 1, 1, 1, 1, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Birthday present from grandparents', 100, 'USD', CURRENT_DATE - 10, NULL, NULL, 0, 3, NULL, 0, 1);
INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Salary', 10000, 'EUR', CURRENT_DATE - 14, NULL, NULL, 1, 1, NULL, 0, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Fringe benefit', 10000, 'HUF', CURRENT_DATE - 14, NULL, NULL, 1, 1, 1, 0, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Found money', 1000, 'HUF', CURRENT_DATE - 5, NULL, NULL, 0, 3, NULL, 0, 1);

-- Lodging regular:
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Lodging', 150000, 'HUF', CURRENT_DATE - 150, NULL, NULL, 1, 4, NULL, 0, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Common cost', 10000, 'HUF', CURRENT_DATE - 150, NULL, NULL, 1, 4, 3, 0, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Overhead', 15000, 'HUF', CURRENT_DATE - 120, NULL, NULL, 1, 4, 4, 0, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Internet', 5000, 'HUF', CURRENT_DATE - 140, NULL, NULL, 1, 4, 5, 0, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Tax', 30000, 'HUF', CURRENT_DATE - 150, NULL, NULL, 1, 4, 6, 0, 1);

-- Car regular:
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Tax', 30, 'EUR', CURRENT_DATE - 100, NULL, NULL, 1, 5, 6, 0, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Insurance', 30, 'EUR', CURRENT_DATE - 100, NULL, NULL, 1, 5, 7, 0, 1);

-- Other regular:
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Massage', 20, 'EUR', CURRENT_DATE - 60, NULL, NULL, 1, 7, 6, 0, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('English', 30, 'USD', CURRENT_DATE - 60, NULL, NULL, 1, 9, 11, 0, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Fitness ticket', 20, 'USD', CURRENT_DATE - 60, NULL, NULL, 1, 11, 15, 0, 1);

-- Car Petrol:
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Petrol', 80, 'EUR', CURRENT_DATE - 100, NULL, NULL, 0, 5, 8, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Petrol', 80, 'EUR', CURRENT_DATE - 80, NULL, NULL, 0, 5, 8, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Petrol', 80, 'EUR', CURRENT_DATE - 60, NULL, NULL, 0, 5, 8, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Petrol', 80, 'EUR', CURRENT_DATE - 40, NULL, NULL, 0, 5, 8, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Petrol', 80, 'EUR', CURRENT_DATE - 20, NULL, NULL, 0, 5, 8, 0, 1);

-- Food:
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Shopping', 10000, 'HUF', CURRENT_DATE - 150, NULL, NULL, 0, 6, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Shopping', 11000, 'HUF', CURRENT_DATE - 140, NULL, NULL, 0, 6, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Shopping', 9000, 'HUF', CURRENT_DATE - 130, NULL, NULL, 0, 6, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Shopping', 8000, 'HUF', CURRENT_DATE - 120, NULL, NULL, 0, 6, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Shopping', 7500, 'HUF', CURRENT_DATE - 110, NULL, NULL, 0, 6, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Shopping', 9000, 'HUF', CURRENT_DATE - 100, NULL, NULL, 0, 6, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Shopping', 11100, 'HUF', CURRENT_DATE - 90, NULL, NULL, 0, 6, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Shopping', 9999, 'HUF', CURRENT_DATE - 80, NULL, NULL, 0, 6, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Shopping', 8675, 'HUF', CURRENT_DATE - 70, NULL, NULL, 0, 6, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Shopping', 12345, 'HUF', CURRENT_DATE - 60, NULL, NULL, 0, 6, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Shopping', 5678, 'HUF', CURRENT_DATE - 50, NULL, NULL, 0, 6, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Shopping', 6245, 'HUF', CURRENT_DATE - 40, NULL, NULL, 0, 6, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Shopping', 12340, 'HUF', CURRENT_DATE - 30, NULL, NULL, 0, 6, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Shopping', 11111, 'HUF', CURRENT_DATE - 20, NULL, NULL, 0, 6, NULL, 0, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Big shopping', 15000, 'HUF', CURRENT_DATE - 10, NULL, NULL, 0, 6, NULL, 0, 1);

INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Hairdresser', 10, 'EUR', CURRENT_DATE - 145, NULL, NULL, 0, 7, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Hairdresser', 10, 'EUR', CURRENT_DATE - 115, NULL, NULL, 0, 7, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Hairdresser', 10, 'EUR', CURRENT_DATE - 85, NULL, NULL, 0, 7, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Hairdresser', 10, 'EUR', CURRENT_DATE - 55, NULL, NULL, 0, 7, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Hairdresser', 10, 'EUR', CURRENT_DATE - 25, NULL, NULL, 0, 7, NULL, 0, 1);

INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Eye examination', 25, 'EUR', CURRENT_DATE - 65, NULL, NULL, 0, 8, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('D vitamin', 5, 'EUR', CURRENT_DATE - 120, NULL, NULL, 0, 8, 10, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('C vitamin', 3, 'EUR', CURRENT_DATE - 120, NULL, NULL, 0, 8, 10, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('D vitamin', 5, 'EUR', CURRENT_DATE - 90, NULL, NULL, 0, 8, 10, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('C vitamin', 3, 'EUR', CURRENT_DATE - 90, NULL, NULL, 0, 8, 10, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('D vitamin', 5, 'EUR', CURRENT_DATE - 60, NULL, NULL, 0, 8, 10, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('C vitamin', 3, 'EUR', CURRENT_DATE - 60, NULL, NULL, 0, 8, 10, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('D vitamin', 5, 'EUR', CURRENT_DATE - 25, NULL, NULL, 0, 8, 10, 0, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('C vitamin', 3, 'EUR', CURRENT_DATE - 25, NULL, NULL, 0, 8, 10, 0, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Online course', 15, 'USD', CURRENT_DATE - 25, NULL, NULL, 0, 8, 10, 0, 1);

INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Aladin', 5, 'EUR', CURRENT_DATE - 65, NULL, NULL, 0, 10, 12, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('Bad boys', 5, 'EUR', CURRENT_DATE - 35, NULL, NULL, 0, 10, 12, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('I am legend', 5, 'EUR', CURRENT_DATE - 15, NULL, NULL, 0, 10, 12, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('New video card', 90, 'USD', CURRENT_DATE - 60, NULL, NULL, 0, 10, 13, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('New convinient keyboard', 20, 'USD', CURRENT_DATE -25, NULL, NULL, 0, 10, 13, 0, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('New running shoes', 40, 'EUR', CURRENT_DATE -55, NULL, NULL, 0, 11, 16, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('New running shirt', 19.99, 'EUR', CURRENT_DATE -55, NULL, NULL, 0, 11, 16, 0, 1);

INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('New jeans', 59.99, 'EUR', CURRENT_DATE -89, NULL, NULL, 0, 12, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('New suit', 100, 'EUR', CURRENT_DATE -72, NULL, NULL, 0, 12, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('New coat', 89.99, 'EUR', CURRENT_DATE -56, NULL, NULL, 0, 12, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('New shirts', 39.98, 'EUR', CURRENT_DATE -35, NULL, NULL, 0, 12, NULL, 1, 1);
INSERT INTO outcomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('New shirt', 35.99, 'EUR', CURRENT_DATE -16, NULL, NULL, 0, 12, NULL, 0, 1);