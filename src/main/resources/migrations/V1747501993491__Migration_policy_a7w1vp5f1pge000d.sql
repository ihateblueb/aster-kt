CREATE TABLE IF NOT EXISTS "policy"
(
	id          VARCHAR(125) PRIMARY KEY,
	"type"      INT           NOT NULL,
	host        VARCHAR(2750) NOT NULL,
	"content"   VARCHAR(5000) NULL,
	"createdAt" TIMESTAMP     NOT NULL,
	"updatedAt" TIMESTAMP     NULL
);
ALTER TABLE "policy"
	ADD CONSTRAINT unique_policy_id UNIQUE (id);
