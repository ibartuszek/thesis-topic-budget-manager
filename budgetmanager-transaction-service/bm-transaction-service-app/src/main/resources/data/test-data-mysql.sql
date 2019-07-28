INSERT INTO main_category (category_type, name) VALUES ('INCOME', 'main income category 1');
INSERT INTO main_category (category_type, name) VALUES ('INCOME', 'main income category 2');
INSERT INTO main_category (category_type, name) VALUES ('INCOME', 'main income category 3');
INSERT INTO main_category (category_type, name) VALUES ('BOTH', 'main category 4');
INSERT INTO main_category (category_type, name) VALUES ('OUTCOME', 'main outcome category 2');
INSERT INTO main_category (category_type, name) VALUES ('OUTCOME', 'main outcome category 3');

INSERT INTO sub_category (category_type, name) VALUES ('INCOME', 'supplementary income category 1');
INSERT INTO sub_category (category_type, name) VALUES ('INCOME', 'supplementary income category 2');
INSERT INTO sub_category (category_type, name) VALUES ('INCOME', 'supplementary income category 3');

INSERT INTO category_join_table (main_category_id, sub_category_id) VALUES (1, 1);
INSERT INTO category_join_table (main_category_id, sub_category_id) VALUES (1, 2);
INSERT INTO category_join_table (main_category_id, sub_category_id) VALUES (2, 3);

INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('a_income', 20000, 'HUF', STR_TO_DATE('23-01-2019', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);
INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('b_income', 20000, 'HUF', STR_TO_DATE('23-01-2019', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);
INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('c_income', 20000, 'HUF', STR_TO_DATE('23-01-2019', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);
INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 

VALUES ('income', 10, 'HUF', STR_TO_DATE('23-01-2019', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);
INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('income', 100, 'HUF', STR_TO_DATE('23-01-2019', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);
INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('income', 999, 'HUF', STR_TO_DATE('23-01-2019', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);

INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('income', 20000, 'HUF', STR_TO_DATE('23-01-2019', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);
INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('income', 20000, 'EUR', STR_TO_DATE('23-01-2019', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);
INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('income', 20000, 'USD', STR_TO_DATE('23-01-2019', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);

INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('income', 20000, 'HUF', STR_TO_DATE('23-01-2017', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);
INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('income', 20000, 'HUF', STR_TO_DATE('22-01-2018', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);
INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('income', 20000, 'HUF', STR_TO_DATE('23-01-2018', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);
INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('income', 20000, 'HUF', STR_TO_DATE('24-01-2018', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);
INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('income', 20000, 'HUF', STR_TO_DATE('23-02-2018', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);
INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('income', 20000, 'HUF', STR_TO_DATE('23-03-2018', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);
INSERT INTO incomes (title, amount, currency, date, description, end_date, monthly, version, main_category_id, sub_category_id) 
VALUES ('income', 20000, 'HUF', STR_TO_DATE('23-01-2019', '%d-%m-%Y'), NULL, NULL, 0, 1, 1, 2);
