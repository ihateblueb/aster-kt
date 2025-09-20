package site.remlit.blueb.aster.db.entity

import org.ktorm.entity.Entity

interface UserPrivateEntity : Entity<UserPrivateEntity> {
	var id: String

	var password: String
	var privateKey: String

	companion object : Entity.Factory<UserPrivateEntity>()
}