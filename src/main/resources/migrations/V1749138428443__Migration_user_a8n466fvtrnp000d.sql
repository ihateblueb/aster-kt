ALTER TABLE "user" ADD "followingUrl" VARCHAR(1025) NOT NULL;
ALTER TABLE "user" ADD "followersUrl" VARCHAR(1025) NOT NULL;
ALTER TABLE "user" ADD CONSTRAINT unique_user_followingUrl UNIQUE ("followingUrl");
ALTER TABLE "user" ADD CONSTRAINT unique_user_followersUrl UNIQUE ("followersUrl");
