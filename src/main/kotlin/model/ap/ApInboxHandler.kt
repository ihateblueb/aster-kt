package site.remlit.blueb.aster.model.ap

import site.remlit.blueb.aster.db.entity.InboxQueueEntity

abstract class ApInboxHandler {
    open suspend fun handle(job: InboxQueueEntity) {}
}
