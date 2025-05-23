package me.blueb.service

import kolbasa.consumer.Message
import kolbasa.consumer.datasource.DatabaseConsumer
import kolbasa.producer.datasource.DatabaseProducer
import kolbasa.queue.PredefinedDataTypes
import kolbasa.queue.Queue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import me.blueb.db.Database
import me.blueb.model.Configuration
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class QueueService {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	private val configuration = Configuration()

	val inboxQueue = Queue.of("inbox", PredefinedDataTypes.String)
	val deliverQueue = Queue.of("deliver", PredefinedDataTypes.String)
	val systemQueue = Queue.of("system", PredefinedDataTypes.String)

	val producer = DatabaseProducer(Database.dataSource)
	val consumer = DatabaseConsumer(Database.dataSource)

	@OptIn(DelicateCoroutinesApi::class)
	fun initConsumers() {
		val inboxThreadPool = newFixedThreadPoolContext(configuration.queue.inbox.threads, "InboxQueueThread")
		val deliverThreadPool = newFixedThreadPoolContext(configuration.queue.deliver.threads, "DeliverQueueThread")
		val systemThreadPool = newFixedThreadPoolContext(configuration.queue.system.threads, "SystemQueueThread")

		GlobalScope.launch(Dispatchers.Default) {
			CoroutineScope(Dispatchers.Default).launch {
				while (true) {
					consumer.receive(inboxQueue, configuration.queue.inbox.concurrency).let { messages ->
						for (message in messages) {
							runBlocking(inboxThreadPool) {
								processInboxJob(message)
							}
						}
					}
					Thread.sleep(500)
				}
			}

			CoroutineScope(Dispatchers.Default).launch {
				while (true) {
					consumer.receive(deliverQueue, configuration.queue.deliver.concurrency).let { messages ->
						for (message in messages) {
							runBlocking(deliverThreadPool) {
								processDeliverJob(message)
							}
						}
					}
					Thread.sleep(500)
				}
			}

			CoroutineScope(Dispatchers.Default).launch {
				while (true) {
					consumer.receive(systemQueue, configuration.queue.system.concurrency).let { messages ->
						for (message in messages) {
							runBlocking(systemThreadPool) {
								processSystemJob(message)
							}
						}
					}
					Thread.sleep(500)
				}
			}
		}

		logger.info("Initialized queue consumers.")
	}

	fun processInboxJob(message: Message<String, Unit>) {
		logger.info("Processing inbox job ${message.id}")
		consumer.delete(inboxQueue, message.id)
	}

	fun processDeliverJob(message: Message<String, Unit>) {
		logger.info("Processing deliver job ${message.id}")
		consumer.delete(deliverQueue, message.id)
	}

	fun processSystemJob(message: Message<String, Unit>) {
		logger.info("Processing system job ${message.id}")
		consumer.delete(systemQueue, message.id)
	}
}
