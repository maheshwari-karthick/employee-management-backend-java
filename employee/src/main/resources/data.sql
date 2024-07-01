CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);


INSERT INTO users (id, username, password)
SELECT 1, 'ADMIN', '$2a$10$GfLnS.3kM/vAK8cMSDWGi.8817Ot.1bLFiARiakbHD1d8dlLS/nJ6'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 1);


CREATE TABLE users_roles
(
    user_id BIGINT,
    roles   VARCHAR(255)
);


INSERT INTO users_roles (user_id, roles)
SELECT 1, 0
WHERE NOT EXISTS (SELECT 1 FROM users_roles WHERE user_id = 1);
