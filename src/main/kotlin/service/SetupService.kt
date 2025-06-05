package site.remlit.blueb.service

import at.favre.lib.crypto.bcrypt.BCrypt
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import site.remlit.blueb.db.entity.RoleEntity
import site.remlit.blueb.db.entity.UserEntity
import site.remlit.blueb.db.entity.UserPrivateEntity
import site.remlit.blueb.db.suspendTransaction
import site.remlit.blueb.db.table.RoleTable
import site.remlit.blueb.db.table.UserTable
import site.remlit.blueb.model.Configuration
import site.remlit.blueb.model.KeyType
import site.remlit.blueb.model.RoleType
import site.remlit.blueb.service.ap.ApIdService

class SetupService {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	val apIdService = ApIdService()

	private val userService = UserService()
	private val keypairService = KeypairService()
	private val identifierService = IdentifierService()
	private val randomService = RandomService()
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
				and (UserTable.host eq null)
		)

		if (existingActor != null) {
			logger.info("Instance actor already exists.")
		} else {
			logger.warn("Instance actor missing, generating...")

			val securePassword = randomService.generateString()

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
					followingUrl = apIdService.renderFollowingApId(id)
					followersUrl = apIdService.renderFollowersApId(id)
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
