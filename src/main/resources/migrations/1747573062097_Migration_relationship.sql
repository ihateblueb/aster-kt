CREATE TABLE IF NOT EXISTS relationship
(
	id           VARCHAR(125)  NOT NULL,
	"type"       INT           NOT NULL,
	"to"         VARCHAR(125)  NOT NULL,
	"from"       VARCHAR(125)  NOT NULL,
	"activityId" VARCHAR(2750) NULL,
	"createdAt"  TIMESTAMP     NOT NULL,
	"updatedAt"  TIMESTAMP     NULL,
	CONSTRAINT fk_relationship_to__id FOREIGN KEY ("to") REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE RESTRICT,
	CONSTRAINT fk_relationship_from__id FOREIGN KEY ("from") REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE RESTRICT
);
ALTER TABLE relationship
	ADD CONSTRAINT unique_relationship_id UNIQUE (id);
