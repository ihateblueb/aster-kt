package site.remlit.blueb.aster.service

import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.IdentifierType
import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.util.aidCounter
import java.util.*

/**
 * Service for generating identifiers.
 *
 * @since 2025.5.1.0-SNAPSHOT
 * */
class IdentifierService : Service() {
	companion object {
		private val aidAlphabet =
			listOf(
				"0",
				"1",
				"2",
				"3",
				"4",
				"5",
				"6",
				"7",
				"8",
				"9",
				"a",
				"b",
				"c",
				"d",
				"e",
				"f",
				"g",
				"h",
				"i",
				"j",
				"k",
				"l",
				"m",
				"n",
				"o",
				"p",
				"q",
				"r",
				"s",
				"t",
				"u",
				"v",
				"w",
				"x",
				"y",
				"z",
			)

		/**
		 * Generate ID with default format
		 *
		 * @return Generated ID
		 * */
		fun generate(): String {
			if (Configuration.identifiers == IdentifierType.Aid)
				return this.generateAid()

			if (Configuration.identifiers == IdentifierType.Aidx)
				return this.generateAidx()

			if (Configuration.identifiers == IdentifierType.Uuid)
				return this.generateUuid()

			return this.generateAidx()
		}

		/**
		 * Generate ID with Aid format
		 *
		 * @return Generated ID
		 * */
		fun generateAid(): String {
			var id = ""

			val now = Date()
			val time2000 = Date(946684800000L)

			var time: Long
			time = (now.time - time2000.time)
			if (time < 0) time = 0

			aidCounter++

			id += time.toString(36).padStart(8, '0')
			id += aidCounter.toString(36).padStart(2, '0').takeLast(2)

			return id
		}

		/**
		 * Generate ID with Aidx format
		 *
		 * @return Generated ID
		 * */
		fun generateAidx(): String {
			var id = ""

			val now = Date()
			val time2000 = Date(946684800000L)

			var time: Long
			time = (now.time - time2000.time)
			if (time < 0) time = 0

			aidCounter++

			id += time.toString(36).padStart(8, '0')
			id += aidAlphabet.random()
			id += aidAlphabet.random()
			id += aidAlphabet.random()
			id += aidAlphabet.random()
			id += aidCounter.toString(36).padStart(4, '0').takeLast(4)

			return id
		}

		/**
		 * Generate ID with Uuid format
		 *
		 * @return Generated ID
		 * */
		fun generateUuid(): String = UUID.randomUUID().toString()
	}
}
