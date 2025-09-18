# Plugins

Aster's system of plugins is similar to how Bukkit (a Minecraft server implementation) works. In the directory where your jar runs, a `plugins` directory is made where you can drop in jar files that are then run by the server.

## Security Considerations

Similar to Bukkit, plugins are not sandboxed. They will have the same privileges that Aster does.