package site.remlit.blueb.aster.service

import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import site.remlit.blueb.aster.db.entity.RelationshipEntity
import site.remlit.blueb.aster.db.suspendTransaction
import site.remlit.blueb.aster.db.table.RelationshipTable
import site.remlit.blueb.aster.model.Relationship
import site.remlit.blueb.aster.model.RelationshipType

class RelationshipService {
	suspend fun get(where: Op<Boolean>): Relationship? = suspendTransaction {
		val relationship = RelationshipEntity
			.find { where }
			.singleOrNull()
			?.load(RelationshipEntity::to, RelationshipEntity::from)


		if (relationship != null)
			Relationship.fromEntity(relationship)
		else null
	}

	suspend fun getActivityId(relationshipId: String): String? = suspendTransaction {
		RelationshipEntity
			.find { RelationshipTable.id eq relationshipId }
			.singleOrNull()
			?.activityId
	}

	/**
	 * Get relationships in both directions for two users
	 * @return [Pair] of [Relationship], where first is to and second is from
	 * */
	suspend fun getPair(to: String, from: String): Pair<Relationship?, Relationship?> {
		return Pair(
			this.get(RelationshipTable.to eq to and (RelationshipTable.from eq from)),
			this.get(RelationshipTable.to eq from and (RelationshipTable.from eq to))
		)
	}

	fun mapPair(pair: Pair<Relationship?, Relationship?>): List<Map<String, Relationship?>> {
		return listOf(
			mapOf(
				Pair(
					"to",
					pair.first
				)
			),
			mapOf(
				Pair(
					"from",
					pair.second
				)
			)
		)
	}

	/**
	 * Determine if there is a [RelationshipType.Block] in either direction
	 * */
	suspend fun eitherBlocking(to: String, from: String): Boolean {
		val pair = this.getPair(to, from)

		if (pair.first != null && pair.first?.type == RelationshipType.Block)
			return true

		if (pair.second != null && pair.first?.type == RelationshipType.Block)
			return true

		return false
	}

	/**
	 * Determine if there is a [RelationshipType.Mute] in one direction
	 * */
	suspend fun muteExists(to: String, from: String): Boolean {
		val relationship = this.get(RelationshipTable.to eq to and (RelationshipTable.from eq from))

		return relationship != null && relationship.type == RelationshipType.Mute
	}
}
