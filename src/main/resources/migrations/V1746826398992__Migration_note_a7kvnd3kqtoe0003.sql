CREATE TABLE IF NOT EXISTS note
(
	id          VARCHAR(100) PRIMARY KEY,
	"apId"      VARCHAR(8192)  NOT NULL,
	"user"      VARCHAR(100)   NOT NULL,
	"content"   VARCHAR(25000) NOT NULL,
	"to"        TEXT[]         NOT NULL,
	tags        TEXT[]         NOT NULL,
	"createdAt" DATE           NOT NULL,
	"updatedAt" DATE           NOT NULL,
	CONSTRAINT fk_note_user__id FOREIGN KEY ("user") REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE RESTRICT
);
ALTER TABLE note
	ADD CONSTRAINT unique_note_id UNIQUE (id);
ALTER TABLE note
	ADD CONSTRAINT unique_note_apId UNIQUE ("apId");
CREATE INDEX note_content_index ON note ("content");
CREATE INDEX note_to_index ON note ("to");
CREATE INDEX note_tag_index ON note (tags);
