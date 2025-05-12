package me.blueb.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SetupService {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	private val userService = UserService()

    suspend fun setup() {
		setupInstanceActor()
	}

    suspend fun setupInstanceActor() {
		val existingActor = userService.getByUsername("instance.actor")

		if (existingActor != null) {
			logger.info("Instance actor present.")
		} else {
			logger.warn("Instance actor missing, generating...")
			println("Instance actor missing, generating...")
		}
	}
}
