ALTER TABLE note
	ADD cw VARCHAR(5000) NULL;
CREATE INDEX note_cw_index ON note (cw);
