CREATE TABLE IF NOT EXISTS "role"
(
	id          VARCHAR(125)  NOT NULL,
	"type"      INT           NOT NULL,
	"name"      VARCHAR(500)  NOT NULL,
	description VARCHAR(2750) NULL,
	"createdAt" TIMESTAMP     NOT NULL,
	"updatedAt" TIMESTAMP     NULL
);
ALTER TABLE "role"
	ADD CONSTRAINT unique_role_id UNIQUE (id);
ALTER TABLE "role"
	ADD CONSTRAINT unique_role_name UNIQUE ("name");
