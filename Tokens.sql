-- SET GLOBAL max_allowed_packet = 1073741824;

-- DROP TABLE `Tokens`;

-- CREATE TABLE `Tokens` (
-- 	`token` VARCHAR(1000),
--  `document` TEXT(20000000)
-- );

-- SELECT COUNT(*) FROM TOKENS;

-- OPTIMIZE TABLE `Tokens`;
-- CREATE INDEX `token_index` ON `Tokens`(`token`) USING BTREE;
-- SELECT * FROM `Tokens` WHERE token = 'anuj';

-- DROP TABLE `H1`;

-- CREATE TABLE `H1` (
-- 	`token` VARCHAR(1000),
--  `document` TEXT(20000000)
-- );

SELECT COUNT(*) FROM H1;

-- OPTIMIZE TABLE `H1`;
-- CREATE INDEX `h1_index` ON `H1`(`token`) USING BTREE;

-- DROP TABLE `H2`;

-- CREATE TABLE `H2` (
-- 	`token` VARCHAR(1000),
--  `document` TEXT(20000000)
-- );

SELECT COUNT(*) FROM H2;

-- OPTIMIZE TABLE `H2`;
-- CREATE INDEX `h2_index` ON `H1`(`token`) USING BTREE;

-- DROP TABLE `H3`;

-- CREATE TABLE `H3` (
-- 	`token` VARCHAR(1000),
--  `document` TEXT(20000000)
-- );

SELECT COUNT(*) FROM H3;

-- OPTIMIZE TABLE `H3`;
-- CREATE INDEX `h3_index` ON `H3`(`token`) USING BTREE;

-- DROP TABLE `B`;

-- CREATE TABLE `B` (
--  `token` VARCHAR(1000),
--  `document` TEXT(20000000)
-- );

SELECT COUNT(*) FROM B;

-- OPTIMIZE TABLE `B`;
-- CREATE INDEX `b_index` ON `B`(`token`) USING BTREE;