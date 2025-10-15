# Aster

Decentralized social software using ActivityPub, written in Kotlin.

## Goals

- Setup and forget. Aster shouldn't be a pain to run.
- Provide typical wants and needs of a social software, and more.
- Allow developing plugins for extending capabilities with ease.
- Make sure users choices are respected, especially blocks and mutes.

## Building

To build Aster, you'll need at least Java 21. You'll also need Node.js (Latest LTS version recommended) and pnpm (
`npm i -g pnpm`).

Run the build script `./gradlew build`, and then grab the JAR from `target/aster-*-all.jar`.

## Running

To run Aster, you just need a PostgreSQL database.
Copy `configuration.example.yaml` to `configuration.yaml` and fill out the connection information. You can edit this
while the server is running, the config is frequently refreshed.
After that, run `java -jar aster.jar migration:execute` and your database will be set up and your instance is ready to
go!

After setting up a user, you can promote them to an Admin role with the CLI. First, get the ID of the generated Admin
role by running `java -jar aster.jar role:list`, and then `java -jar aster.jar role:give {User ID} {Role ID}`.

## Contributing

Contributions are welcome, but Aster is in early development and I may have plans for how to do things already. You
should contact me before opening a pull request or working on anything so we can get on the same page.

### Development Environment

IntelliJ IDEA is a requirement for working with Aster. For development, the JetBrains Runtime is recommended.

When you clone Aster in IDEA, automatically it should recognize the build scripts in `.run`. Follow the building and
running instructions above to create a JAR and prepare your database.

Afterward, running `Backend Development Build` in the IDE should be all you need to develop, unless you need to use CLI
commands. This script skips generating documentation and compiling the frontend, which significantly slows down the
build time.

If you are working on the frontend, running `pnpm dev` in the `frontend` directory will start a
development server for the frontend which hot reloads.