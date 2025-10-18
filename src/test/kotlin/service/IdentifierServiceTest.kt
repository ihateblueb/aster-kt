package site.remlit.blueb.aster.test.basic

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import site.remlit.blueb.aster.model.IdentifierType
import site.remlit.blueb.aster.service.IdentifierService
import site.remlit.blueb.aster.util.median
import kotlin.test.Test
import kotlin.time.measureTimedValue

class IdentifierServiceTest {

	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	private fun noDuplicatesGenerate(type: IdentifierType, repeat: Int): List<Pair<Long, String>> {
		/**
		 * First is time taken to generate, second is result
		 * */
		val results = mutableListOf<Pair<Long, String>>()

		repeat(repeat) {
			var result: String
			val timeTaken = measureTimedValue {
				result = when (type) {
					IdentifierType.Aid -> IdentifierService.generateAid()
					IdentifierType.Aidx -> IdentifierService.generateAidx()
					IdentifierType.Uuid -> IdentifierService.generateUuid()
				}
			}
			results.add(Pair(timeTaken.duration.inWholeMicroseconds, result))
		}

		return results
	}

	private fun noDuplicates(count: Int) {
		val results =
			mutableListOf<Pair<IdentifierType, List<Pair<Long, String>>>>()

		for (type in IdentifierType.entries) {
			results.add(Pair(type, noDuplicatesGenerate(type, count)))
		}

		for (result in results) {
			val durations = mutableListOf<Long>()
			val ids = mutableListOf<String>()

			for (g in result.second) {
				durations.add(g.first)
				ids.add(g.second)
			}

			val avg = durations.average()
			val min = durations.min()
			val max = durations.max()
			val median = durations.median()

			var idsUniqueIterations = 0
			val idsUnique = mutableListOf<String>()

			for (id in ids) {
				idsUniqueIterations++
				if (idsUnique.contains(id)) {
					logger.error("Duplicate ID of type ${result.first} found on iteration $idsUniqueIterations.")
					break
				}
				idsUnique.add(id)
			}

			logger.info("[$count:${idsUnique.size}] ${result.first}: AVG ${avg}μs, MIN ${min}μs, MAX ${max}μs, MEDIAN ${median}μs")
		}
	}

	@Test
	fun noDuplicates10() = noDuplicates(10)

	@Test
	fun noDuplicates100() = noDuplicates(100)

	@Test
	fun noDuplicates10000() = noDuplicates(10000)

}