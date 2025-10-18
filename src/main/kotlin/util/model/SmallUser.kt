package site.remlit.blueb.aster.util.model

import site.remlit.blueb.aster.common.model.SmallUser
import site.remlit.blueb.aster.common.model.User

fun SmallUser.Companion.fromUser(user: User): SmallUser {
	return SmallUser(
		id = user.id,
		apId = user.apId,
		username = user.username,
		host = user.host,
		displayName = user.displayName,
		avatar = user.avatar,
		avatarAlt = user.avatarAlt,
		automated = user.automated,
		sensitive = user.sensitive,
		isCat = user.isCat,
		createdAt = user.createdAt,
		updatedAt = user.updatedAt
	)
}