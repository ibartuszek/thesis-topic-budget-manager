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
