package me.blueb.service

import kolbasa.consumer.Message
import kolbasa.consumer.ReceiveOptions
import kolbasa.consumer.datasource.Consumer
import kolbasa.producer.Id
import kolbasa.producer.SendRequest
import kolbasa.producer.SendResult
import kolbasa.producer.datasource.Producer
import kolbasa.queue.PredefinedDataTypes
import kolbasa.queue.Queue
import java.util.concurrent.CompletableFuture

class QueueService {
	val inboxQueue = Queue.of("inbox", PredefinedDataTypes.String)
	val deliverQueue = Queue.of("deliver", PredefinedDataTypes.String)
	val systemQueue = Queue.of("system", PredefinedDataTypes.String)
}
