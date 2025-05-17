package me.blueb.service

import kolbasa.consumer.Message
import kolbasa.consumer.datasource.DatabaseConsumer
import kolbasa.producer.datasource.DatabaseProducer
import kolbasa.queue.PredefinedDataTypes
import kolbasa.queue.Queue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import me.blueb.db.Database
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class QueueService {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	val inboxQueue = Queue.of("inbox", PredefinedDataTypes.String)
	val deliverQueue = Queue.of("deliver", PredefinedDataTypes.String)
	val systemQueue = Queue.of("system", PredefinedDataTypes.String)

	val producer = DatabaseProducer(Database.dataSource)
	val consumer = DatabaseConsumer(Database.dataSource)

	@OptIn(DelicateCoroutinesApi::class)
	val inboxThreadPool = newFixedThreadPoolContext(8, "InboxQueueThread")

	@OptIn(DelicateCoroutinesApi::class)
	val deliverThreadPool = newFixedThreadPoolContext(6, "DeliverQueueThread")

	@OptIn(DelicateCoroutinesApi::class)
	val systemThreadPool = newFixedThreadPoolContext(4, "SystemQueueThread")

	fun initConsumers() {
		consumer.receive(inboxQueue)?.let { message ->
			runBlocking(inboxThreadPool) {
				processInboxJob(message)
			}
		}

		consumer.receive(deliverQueue)?.let { message ->
			runBlocking(deliverThreadPool) {
				processDeliverJob(message)
			}
		}

		consumer.receive(systemQueue)?.let { message ->
			runBlocking(systemThreadPool) {
				processSystemJob(message)
			}
		}
	}

	fun processInboxJob(message: Message<String, Unit>) {
		logger.info("Processing inbox job ${message.id}")
	}

	fun processDeliverJob(message: Message<String, Unit>) {
		logger.info("Processing deliver job ${message.id}")
	}

	fun processSystemJob(message: Message<String, Unit>) {
		logger.info("Processing system job ${message.id}")
	}
}
