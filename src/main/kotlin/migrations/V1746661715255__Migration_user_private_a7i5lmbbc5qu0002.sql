CREATE TABLE IF NOT EXISTS user_private (id VARCHAR(100) NOT NULL, "password" VARCHAR(8192) NOT NULL);
ALTER TABLE user_private ADD CONSTRAINT unique_user_private_id UNIQUE (id);
