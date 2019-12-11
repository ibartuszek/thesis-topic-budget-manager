INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'supplementary category 1', 'INCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'supplementary category 2', 'INCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'supplementary category 3', 'INCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'supplementary category 2', 'OUTCOME', 1);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'supplementary category 3', 'OUTCOME', 1);

INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'supplementary category 1', 'INCOME', 2);
INSERT INTO sub_category(id, name, transaction_type, user_id) VALUES (NULL, 'supplementary category 2', 'OUTCOME', 2);

INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'main category 1', 'INCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'main category 2', 'INCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'main category 3', 'INCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'main category 2', 'OUTCOME', 1);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'main category 3', 'OUTCOME', 1);

INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'main category 1', 'INCOME', 2);
INSERT INTO main_category(id, name, transaction_type, user_id) VALUES (NULL, 'main category 2', 'OUTCOME', 2);

INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (1, 1);
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (1, 2);


INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('income 1', 1000, 'EUR', CURRENT_DATE - 20, NULL, NULL, 0, 1, NULL, 1, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('income 2', 1000, 'EUR', CURRENT_DATE - 20, NULL, NULL, 0, 1, 1, 1, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('income 3', 1000, 'EUR', CURRENT_DATE - 10, NULL, NULL, 0, 1, 2, 1, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('income 4', 1000, 'EUR', CURRENT_DATE - 10, NULL, NULL, 0, 2, NULL, 1, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('income 1', 1000, 'EUR', CURRENT_DATE - 5, NULL, NULL, 0, 1, NULL, 0, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('income 2', 1000, 'EUR', CURRENT_DATE - 5, NULL, NULL, 0, 1, 1, 0, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('income 3', 1000, 'EUR', CURRENT_DATE - 5, NULL, NULL, 0, 1, 2, 0, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('income 4', 1000, 'EUR', CURRENT_DATE - 5, NULL, NULL, 0, 2, NULL, 0, 1);

INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked, user_id)
VALUES ('income 1', 1000, 'EUR', CURRENT_DATE - 20, NULL, NULL, 0, 1, NULL, 1, 2);
