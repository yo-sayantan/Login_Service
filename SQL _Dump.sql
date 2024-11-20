CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL
);

INSERT INTO users (username, password) VALUES ('admin', 'admin'), ('john_doe', 'password123');
INSERT INTO users (username, password) VALUES 
-- ('admin2', '{noop}ad-- min2'),
-- ('test', 'test'),
('admin', '$2a$10$6Q.yP/7wFUEX9rP5LsCQRO1TUnniNp4mZNUkT/Fk9N8e0U/aPb76.');
('admin1', '$2a$10$0Gb0Twrxh.L0VrY2w68bme1V2IdTeOp3HeF4ez1ppzzncbzxzXmuO'), -- BCrypt hashed password for "admin123"
('john_doe1', '$2a$10$6kK3E3HekZ8OfNKnQoFsxOba8Hx24j3EMfEhkHPeoWyIajW1JMGYS'); -- BCrypt hashed password for "password123"
drop table roles;
INSERT INTO users (username, password) VALUES ('testuser', 'testpass');

INSERT INTO roles (name) VALUES 
('ADMIN'),
('USER');

INSERT INTO user_roles (user_id, role_id) VALUES 
-- (1, 1), -- Admin user assigned ROLE_ADMIN
-- (2, 2),
(5, 1); -- John Doe assigned ROLE_USER

TRUNCATE users;

SELECT u.username, u.password, r.name AS role
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id;

select * from users;
select * from roles;
select * from user_roles;

alter table users drop column roles;
commit;