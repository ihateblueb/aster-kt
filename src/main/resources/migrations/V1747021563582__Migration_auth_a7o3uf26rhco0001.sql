CREATE TABLE IF NOT EXISTS auth
(
	id          VARCHAR(125) PRIMARY KEY,
	token       VARCHAR(275) NOT NULL,
	"user"      VARCHAR(125) NOT NULL,
	"createdAt" DATE         NOT NULL,
	CONSTRAINT fk_auth_user__id FOREIGN KEY ("user") REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE RESTRICT
);
ALTER TABLE auth
	ADD CONSTRAINT unique_auth_id UNIQUE (id);
ALTER TABLE auth
	ADD CONSTRAINT unique_auth_token UNIQUE (token);
