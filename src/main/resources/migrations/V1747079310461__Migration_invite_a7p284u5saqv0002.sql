CREATE TABLE IF NOT EXISTS invite
(
	id          VARCHAR(125) PRIMARY KEY,
	code        VARCHAR(275) NOT NULL,
	"user"      VARCHAR(125) NOT NULL,
	creator     VARCHAR(125) NOT NULL,
	"createdAt" DATE         NOT NULL,
	CONSTRAINT fk_invite_user__id FOREIGN KEY ("user") REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE RESTRICT,
	CONSTRAINT fk_invite_creator__id FOREIGN KEY (creator) REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE RESTRICT
);
ALTER TABLE invite
	ADD CONSTRAINT unique_invite_id UNIQUE (id);
ALTER TABLE invite
	ADD CONSTRAINT unique_invite_code UNIQUE (code);
