package me.blueb.service

import at.favre.lib.crypto.bcrypt.BCrypt
import me.blueb.db.entity.RoleEntity
import me.blueb.db.entity.UserEntity
import me.blueb.db.entity.UserPrivateEntity
import me.blueb.db.suspendTransaction
import me.blueb.db.table.RoleTable
import me.blueb.db.table.UserTable
import me.blueb.model.Configuration
import me.blueb.model.KeyType
import me.blueb.model.RoleType
import me.blueb.service.ap.ApIdService
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigInteger
import java.security.SecureRandom

class SetupService {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	val apIdService = ApIdService()

	private val userService = UserService()
	private val keypairService = KeypairService()
	private val identifierService = IdentifierService()
	private val roleService = RoleService()

	private val configuration = Configuration()

    suspend fun setup() {
		setupRoles()
		setupInstanceActor()
	}

	suspend fun setupRoles() {
		val existingAdminRole = roleService.get(RoleTable.type eq RoleType.Admin)

		if (existingAdminRole != null) {
			logger.info("Admin role already exists.")
		} else {
			logger.warn("Admin role missing, generating...")

			val id = identifierService.generate()

			suspendTransaction {
				RoleEntity.new(id) {
					name = "Admin"
					type = RoleType.Admin
				}
			}

			logger.info("Admin role generated.")
		}

		val existingModRole = roleService.get(RoleTable.type eq RoleType.Mod)

		if (existingModRole != null) {
			logger.info("Mod role already exists.")
		} else {
			logger.warn("Mod role missing, generating...")

			val id = identifierService.generate()

			suspendTransaction {
				RoleEntity.new(id) {
					name = "Mod"
					type = RoleType.Mod
				}
			}

			logger.info("Mod role generated.")
		}
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
					apId = apIdService.renderUserApId(id)
					inbox = apIdService.renderInboxApId(id)
					outbox = apIdService.renderOutboxApId(id)
					username = "instance.actor"
					activated = true
					automated = true
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
