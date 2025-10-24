package site.remlit.aster.service

import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.load
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import site.remlit.aster.common.model.Relationship
import site.remlit.aster.common.model.type.RelationshipType
import site.remlit.aster.db.entity.RelationshipEntity
import site.remlit.aster.db.table.RelationshipTable
import site.remlit.aster.model.Service
import site.remlit.aster.util.model.fromEntity

/**
 * Service for managing user relationships.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class RelationshipService : Service() {
	companion object {
		/**
		 * Get a relationship.
		 *
		 * @param where Query to find relationship
		 *
		 * @return Found relationship, if any
		 * */
		fun get(where: Op<Boolean>): Relationship? = transaction {
			val relationship = RelationshipEntity
				.find { where }
				.singleOrNull()
				?.load(RelationshipEntity::to, RelationshipEntity::from)


			if (relationship != null)
				Relationship.fromEntity(relationship)
			else null
		}

		/**
		 * Gets ID of activity used to create a relationship
		 *
		 * @param relationshipId ID of the relationship
		 *
		 * @return ActivityPub ID of the activity used to create the relationship
		 * */
		fun getActivityId(relationshipId: String): String? = transaction {
			RelationshipEntity
				.find { RelationshipTable.id eq relationshipId }
				.singleOrNull()
				?.activityId
		}

		/**
		 * Get relationships in both directions for two users
		 *
		 * @param to Relationship target
		 * @param from Relationship owner
		 *
		 * @return [Pair] of [site.remlit.aster.model.Relationship], where first is to and second is from
		 * */
		fun getPair(to: String, from: String): Pair<Relationship?, Relationship?> {
			return Pair(
				this.get(RelationshipTable.to eq to and (RelationshipTable.from eq from)),
				this.get(RelationshipTable.to eq from and (RelationshipTable.from eq to))
			)
		}

		fun mapPair(pair: Pair<Relationship?, Relationship?>): List<Map<String, Relationship?>> {
			return listOf(
				mapOf(
					"to" to pair.first
				),
				mapOf(
					"from" to pair.second
				)
			)
		}

		/**
		 * Determine if there is a [site.remlit.aster.model.RelationshipType.Block] in either direction
		 *
		 * @param to First user
		 * @param from Second user
		 *
		 * @return If either are blocking each other
		 * */
		fun eitherBlocking(to: String, from: String): Boolean {
			val pair = this.getPair(to, from)

			if (pair.first != null && pair.first?.type == RelationshipType.Block)
				return true

			if (pair.second != null && pair.first?.type == RelationshipType.Block)
				return true

			return false
		}

		/**
		 * Determine if there is a [site.remlit.aster.model.RelationshipType.Mute] in one direction
		 *
		 * @param to Relationship target
		 * @param from Relationship owner
		 *
		 * @return If `from` is muting `to`
		 * */
		fun muteExists(to: String, from: String): Boolean {
			val relationship = this.get(RelationshipTable.to eq to and (RelationshipTable.from eq from))

			return relationship != null && relationship.type == RelationshipType.Mute
		}
	}
}
