INSERT INTO sub_category(id, name, transaction_type) VALUES (NULL, 'supplementary category 1', 'INCOME');
INSERT INTO sub_category(id, name, transaction_type) VALUES (NULL, 'supplementary category 2', 'INCOME');
INSERT INTO sub_category(id, name, transaction_type) VALUES (NULL, 'supplementary category 3', 'INCOME');
INSERT INTO sub_category(id, name, transaction_type) VALUES (NULL, 'supplementary category 2', 'OUTCOME');
INSERT INTO sub_category(id, name, transaction_type) VALUES (NULL, 'supplementary category 3', 'OUTCOME');

INSERT INTO main_category(id, name, transaction_type) VALUES (NULL, 'main category 1', 'INCOME');
INSERT INTO main_category(id, name, transaction_type) VALUES (NULL, 'main category 2', 'INCOME');
INSERT INTO main_category(id, name, transaction_type) VALUES (NULL, 'main category 3', 'INCOME');
INSERT INTO main_category(id, name, transaction_type) VALUES (NULL, 'main category 2', 'OUTCOME');
INSERT INTO main_category(id, name, transaction_type) VALUES (NULL, 'main category 3', 'OUTCOME');

INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (1, 1);
INSERT INTO category_join_table(main_category_id, sub_category_id) VALUES (1, 2);


INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked)
VALUES ('income 1', 1000, 'EUR', CURRENT_DATE - 20, NULL, NULL, 0, 1, NULL, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked)
VALUES ('income 2', 1000, 'EUR', CURRENT_DATE - 20, NULL, NULL, 0, 1, 1, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked)
VALUES ('income 3', 1000, 'EUR', CURRENT_DATE - 10, NULL, NULL, 0, 1, 2, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked)
VALUES ('income 4', 1000, 'EUR', CURRENT_DATE - 10, NULL, NULL, 0, 2, NULL, 1);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked)
VALUES ('income 1', 1000, 'EUR', CURRENT_DATE - 5, NULL, NULL, 0, 1, NULL, 0);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked)
VALUES ('income 2', 1000, 'EUR', CURRENT_DATE - 5, NULL, NULL, 0, 1, 1, 0);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked)
VALUES ('income 3', 1000, 'EUR', CURRENT_DATE - 5, NULL, NULL, 0, 1, 2, 0);
INSERT INTO incomes
(title, amount, currency, date, description, end_date, monthly, main_category_id, sub_category_id, locked)
VALUES ('income 4', 1000, 'EUR', CURRENT_DATE - 5, NULL, NULL, 0, 2, NULL, 0);
