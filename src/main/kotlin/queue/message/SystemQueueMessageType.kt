package site.remlit.blueb.aster.queue.message

enum class SystemQueueMessageType {
	// check for policy and then apply to all notes
	SyncPolicyNotes,

	// check for policy and then apply to all users
	SyncPolicyUsers
}
