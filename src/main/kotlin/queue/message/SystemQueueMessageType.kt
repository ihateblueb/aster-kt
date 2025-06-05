package site.remlit.blueb.queue.message

enum class SystemQueueMessageType {
	// check for policy and then apply to all notes
	SyncPolicyNotes,

	// check for policy and then apply to all users
	SyncPolicyUsers
}
