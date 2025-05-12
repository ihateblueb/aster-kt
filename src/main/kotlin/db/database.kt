package me.blueb.db

import me.blueb.model.Configuration
import org.jetbrains.exposed.sql.Database

val configuration = Configuration()

val database = Database.connect(
	"jdbc:postgresql://${configuration.database.host}:${configuration.database.port}/${configuration.database.db}",
	user = configuration.database.user,
	password = configuration.database.password,
)
