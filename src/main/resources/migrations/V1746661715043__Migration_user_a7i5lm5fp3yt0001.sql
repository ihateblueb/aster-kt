CREATE TABLE IF NOT EXISTS "user"
(
	id            VARCHAR(100) PRIMARY KEY,
	"apId"        VARCHAR(8192)         NOT NULL,
	inbox         VARCHAR(8192)         NOT NULL,
	outbox        VARCHAR(8192)         NULL,
	username      VARCHAR(175)          NOT NULL,
	host          VARCHAR(500)          NULL,
	"displayName" VARCHAR(500)          NULL,
	bio           VARCHAR(8192)         NULL,
	"location"    VARCHAR(500)          NULL,
	birthday      VARCHAR(500)          NULL,
	avatar        VARCHAR(500)          NULL,
	"avatarAlt"   VARCHAR(8192)         NULL,
	banner        VARCHAR(500)          NULL,
	"bannerAlt"   VARCHAR(8192)         NULL,
	"locked"      BOOLEAN DEFAULT FALSE NOT NULL,
	suspended     BOOLEAN DEFAULT FALSE NOT NULL,
	activated     BOOLEAN DEFAULT FALSE NOT NULL,
	discoverable  BOOLEAN DEFAULT FALSE NOT NULL,
	indexable     BOOLEAN DEFAULT FALSE NOT NULL,
	"sensitive"   BOOLEAN DEFAULT FALSE NOT NULL,
	"isCat"       BOOLEAN DEFAULT FALSE NOT NULL,
	"speakAsCat"  BOOLEAN DEFAULT FALSE NOT NULL
);
ALTER TABLE "user"
	ADD CONSTRAINT unique_user_id UNIQUE (id);
ALTER TABLE "user"
	ADD CONSTRAINT unique_user_apId UNIQUE ("apId");
ALTER TABLE "user"
	ADD CONSTRAINT unique_user_inbox UNIQUE (inbox);
