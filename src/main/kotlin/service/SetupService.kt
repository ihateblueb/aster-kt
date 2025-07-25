package site.remlit.blueb.aster.service

import at.favre.lib.crypto.bcrypt.BCrypt
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.db.entity.RoleEntity
import site.remlit.blueb.aster.db.entity.UserEntity
import site.remlit.blueb.aster.db.entity.UserPrivateEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.db.table.RoleTable
import site.remlit.blueb.aster.db.table.UserTable
import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.KeyType
import site.remlit.blueb.aster.model.RoleType
import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.service.ap.ApIdService

class SetupService : Service() {
	companion object {
		private val logger: Logger = LoggerFactory.getLogger(this::class.java)

		private val configuration = Configuration()

		suspend fun setup() {
			setupRoles()
			setupInstanceActor()
		}

		suspend fun setupRoles() {
			val existingAdminRole = RoleService.get(RoleTable.type eq RoleType.Admin)

			if (existingAdminRole != null) {
				logger.info("Admin role already exists.")
			} else {
				logger.warn("Admin role missing, generating...")

				val id = IdentifierService.generate()

				suspendTransaction {
					RoleEntity.new(id) {
						name = "Admin"
						type = RoleType.Admin
					}
				}

				logger.info("Admin role generated.")
			}

			val existingModRole = RoleService.get(RoleTable.type eq RoleType.Mod)

			if (existingModRole != null) {
				logger.info("Mod role already exists.")
			} else {
				logger.warn("Mod role missing, generating...")

				val id = IdentifierService.generate()

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
			val existingActor = UserService.get(
				UserTable.username eq "instance.actor"
					and (UserTable.host eq null)
			)

			if (existingActor != null) {
				logger.info("Instance actor already exists.")
			} else {
				logger.warn("Instance actor missing, generating...")

				val securePassword = RandomService.generateString()

				val id = IdentifierService.generate()
				val hashedPassword = BCrypt.withDefaults().hashToString(12, securePassword.toCharArray())

				val keypair = KeypairService.generate()

				suspendTransaction {
					UserEntity.new(id) {
						apId = ApIdService.renderUserApId(id)
						inbox = ApIdService.renderInboxApId(id)
						outbox = ApIdService.renderOutboxApId(id)
						username = "instance.actor"
						activated = true
						automated = true
						followingUrl = ApIdService.renderFollowingApId(id)
						followersUrl = ApIdService.renderFollowersApId(id)
						publicKey = KeypairService.keyToPem(KeyType.Public, keypair)
					}
					UserPrivateEntity.new(id) {
						password = hashedPassword
						privateKey = KeypairService.keyToPem(KeyType.Private, keypair)
					}
				}

				logger.info("Instance actor generated.")
			}
		}
	}
}
