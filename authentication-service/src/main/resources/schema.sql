DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS user_org;

CREATE  TABLE `user` (
    user_name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled boolean NOT NULL,
    PRIMARY KEY (user_name)
);

CREATE TABLE user_role (
    user_role_id SERIAL,
    user_name varchar(100) NOT NULL,
    role varchar(100) NOT NULL,
    PRIMARY KEY (user_role_id)
);

CREATE TABLE user_org (
    organization_id   VARCHAR(100)  NOT NULL,
    user_name         VARCHAR(100)  NOT NULL,
    PRIMARY KEY (user_name)
);

INSERT INTO `user` (user_name, password, enabled) VALUES ('kimbs','$2a$04$NX3QTkBJB00upxKeaKqFBeoIVc9JHvwVnj1lItxNphRj34wNx5wlu', true);
INSERT INTO `user` (user_name, password, enabled) VALUES ('william.woodward','$2a$04$lM2hIsZVNYrQLi8mhvnTA.pheZtmzeivz6fyxCr9GZ6YSfP6YibCW', true);

INSERT INTO user_role (user_name, role) VALUES ('kimbs', 'ROLE_USER');
INSERT INTO user_role (user_name, role) VALUES ('kimbs', 'ROLE_ADMIN');
INSERT INTO user_role (user_name, role) VALUES ('william.woodward', 'ROLE_USER');

INSERT INTO user_org (organization_id, user_name) VALUES ('d1859f1f-4bd7-4593-8654-ea6d9a6a626e', 'kimbs');
INSERT INTO user_org (organization_id, user_name) VALUES ('42d3d4f5-9f33-42f4-8aca-b7519d6af1bb', 'william.woodward');