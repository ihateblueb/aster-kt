package me.blueb.service

import kolbasa.queue.PredefinedDataTypes
import kolbasa.queue.Queue

class QueueService {
	val inboxQueue = Queue.of("inbox", PredefinedDataTypes.String)
	val deliverQueue = Queue.of("deliver", PredefinedDataTypes.String)
	val systemQueue = Queue.of("system", PredefinedDataTypes.String)
}
