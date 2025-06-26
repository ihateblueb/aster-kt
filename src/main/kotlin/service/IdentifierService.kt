package site.remlit.blueb.aster.service

import site.remlit.blueb.aster.model.Configuration
import site.remlit.blueb.aster.model.IdentifierType
import site.remlit.blueb.aster.model.Service
import site.remlit.blueb.aster.util.aidCounter
import java.util.*

class IdentifierService : Service() {
	companion object {
		private val configuration = Configuration()

		var aidAlphabet =
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

		fun generate(): String {
			if (configuration.identifiers == IdentifierType.Aid)
				return this.generateAid()

			if (configuration.identifiers == IdentifierType.Aidx)
				return this.generateAidx()

			if (configuration.identifiers == IdentifierType.Uuid)
				return UUID.randomUUID().toString()

			return this.generateAidx()
		}

		fun generateAid(): String {
			var id: String = ""

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

		fun generateAidx(): String {
			var id: String = ""

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
	}
}
