# Plugins

Aster's system of plugins is similar to how Bukkit (a Minecraft server implementation) works. In the directory where
your jar runs, a `plugins` directory is made where you can drop in jar files that are then run by the server.

Plugins are the first thing that are loaded after the server starts. The database and HTTP server are not started until
plugins finish loading.

## Level of Extendability

Aster has an internal event system for acting on global events and multiple registries for adding function. For example,
when a note is created, an event is fired. For more on plugin development, refer to `plugin-development.md`.

## Security Considerations

Similar to Bukkit, plugins are not sandboxed. They will have the same privileges that Aster does. Any plugin has the
potential to be malicious, make sure you're downloading them from trusted sources. If you know how, inspect the compiled
code yourself.