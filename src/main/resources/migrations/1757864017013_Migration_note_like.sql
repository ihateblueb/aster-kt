CREATE TABLE IF NOT EXISTS note_like
(
    id          VARCHAR(125) PRIMARY KEY,
    "user"      VARCHAR(125)                        NOT NULL,
    note        VARCHAR(125)                        NOT NULL,
    "createdAt" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
ALTER TABLE note_like
    ADD CONSTRAINT unique_note_like_id UNIQUE (id);
ALTER TABLE note_like
    ADD CONSTRAINT fk_note_like_user__id FOREIGN KEY ("user") REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE note_like
    ADD CONSTRAINT fk_note_like_note__id FOREIGN KEY (note) REFERENCES note (id) ON DELETE CASCADE ON UPDATE RESTRICT;
