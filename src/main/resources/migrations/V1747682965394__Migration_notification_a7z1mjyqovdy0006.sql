CREATE TABLE IF NOT EXISTS notification
(
	id           VARCHAR(125) PRIMARY KEY,
	"type"       INT                                 NOT NULL,
	"to"         VARCHAR(125)                        NOT NULL,
	"from"       VARCHAR(125)                        NOT NULL,
	note         VARCHAR(125)                        NOT NULL,
	relationship VARCHAR(125)                        NOT NULL,
	"createdAt"  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
ALTER TABLE notification
	ADD CONSTRAINT unique_notification_id UNIQUE (id);
ALTER TABLE notification
	ADD CONSTRAINT fk_notification_to__id FOREIGN KEY ("to") REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE notification
	ADD CONSTRAINT fk_notification_from__id FOREIGN KEY ("from") REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE notification
	ADD CONSTRAINT fk_notification_note__id FOREIGN KEY (note) REFERENCES note (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE notification
	ADD CONSTRAINT fk_notification_relationship__id FOREIGN KEY (relationship) REFERENCES relationship (id) ON DELETE CASCADE ON UPDATE RESTRICT;
