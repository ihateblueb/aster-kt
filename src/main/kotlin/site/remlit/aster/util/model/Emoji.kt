package site.remlit.aster.util.model

import site.remlit.aster.common.model.Emoji
import site.remlit.aster.db.entity.EmojiEntity

fun Emoji.Companion.fromEntity(entity: EmojiEntity): Emoji =
	Emoji(
		id = entity.id.toString(),
		apId = entity.apId,
		name = entity.name,
		host = entity.host,
		src = entity.src,
		createdAt = entity.createdAt,
		updatedAt = entity.updatedAt,
	)

fun Emoji.Companion.fromEntities(entities: List<EmojiEntity>): List<Emoji> =
	entities.map { fromEntity(it) }
