package site.remlit.blueb.aster.db.entity

import org.ktorm.entity.Entity

interface UserPrivateEntity : Entity<UserPrivateEntity> {
	val id: String
	var password: String
	val privateKey: String
}