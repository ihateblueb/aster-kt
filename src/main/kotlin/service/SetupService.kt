package me.blueb.service

import at.favre.lib.crypto.bcrypt.BCrypt
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.blueb.db.entity.UserEntity
import me.blueb.db.entity.UserPrivateEntity
import me.blueb.db.suspendTransaction
import me.blueb.db.table.UserTable
import me.blueb.model.Configuration
import me.blueb.model.InstanceRegistrationsType
import me.blueb.model.KeyType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigInteger
import java.security.SecureRandom

class SetupService {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	private val userService = UserService()
	private val keypairService = KeypairService()
	private val identifierService = IdentifierService()

	private val configuration = Configuration()

    suspend fun setup() {
		setupInstanceActor()
	}

	suspend fun setupInstanceActor() {
		val existingActor = userService.get(
			UserTable.username eq "instance.actor"
				and(UserTable.host eq null)
		)

		if (existingActor != null) {
			logger.info("Instance actor already exists.")
		} else {
			logger.warn("Instance actor missing, generating...")

			val random = SecureRandom()

			val bytes = ByteArray(16)
			random.nextBytes(bytes)

			val securePassword = BigInteger(1, bytes)
				.toString(32)
				.padStart(16, '0')

			val id = identifierService.generate()
			val hashedPassword = BCrypt.withDefaults().hashToString(12, securePassword.toCharArray())

			val keypair = keypairService.generate()

			suspendTransaction {
				UserEntity.new(id) {
					apId = configuration.url.toString() + "user/" + id
					inbox = configuration.url.toString() + "user/" + id + "/inbox"
					outbox = configuration.url.toString() + "user/" + id + "/outbox"
					username = "instance.actor"
					activated = configuration.registrations != InstanceRegistrationsType.Approval
					createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
					publicKey = keypairService.keyToPem(KeyType.Public, keypair)
				}
				UserPrivateEntity.new(id) {
					password = hashedPassword
					privateKey = keypairService.keyToPem(KeyType.Private, keypair)
				}
			}

			logger.info("Instance actor generated.")
		}
	}
}
