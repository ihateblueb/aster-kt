import * as Common from 'aster-common'
import './Notification.scss'

function Notification(
    {data}:
    {
        data: Common.Notification,
    }
) {

    function renderName() {
        return <b>{data.from.displayName ?? data.from.username}</b>
    }

    function renderTitle() {
        switch (data.type) {
            case Common.NotificationType.Like:
                return <>
                    <span>{renderName()} liked your post</span>
                </>
            case Common.NotificationType.React:
                return <>
                    <span>{renderName()} reacted to your post</span>
                </>
            case Common.NotificationType.Follow:
                return <>
                    <span>{renderName()} followed you</span>
                </>
            case Common.NotificationType.AcceptedFollow:
                return <>
                    <span>{renderName()} accepted your follow request</span>
                </>
            case Common.NotificationType.Mention:
                return <>
                    <span>{renderName()} mentioned you</span>
                </>
            case Common.NotificationType.Announcement:
                return <>
                    <span>{/* announcement title */}</span>
                </>
            case Common.NotificationType.BrokenRelationship:
                return <>
                    <span>One or more relationship broke.</span>
                </>
            case Common.NotificationType.Bite:
                return <>
                    <span>{renderName()} bit you</span>
                </>
            case Common.NotificationType.Registration:
                return <>
                    <span>{renderName()} registered</span>
                </>
            case Common.NotificationType.Report:
                return <>
                    <span>{renderName()} submitted a report</span>
                </>
            default:
                return null
        }
    }

    function renderBody() {
        switch (data.type) {
            case Common.NotificationType.Like:
                return <>
                    <span>TODO: Small note</span>
                </>
            case Common.NotificationType.React:
                return <>
                    <span>TODO: Small note</span>
                </>
            case Common.NotificationType.Mention:
                return <>
                    <span>TODO: Small note</span>
                </>
            case Common.NotificationType.Bite:
                return data.note ? <>
                    <span>TODO: Small note</span>
                </> : null
            default:
                return null
        }
    }

    return (
        <div className={`notification`}>
            {renderTitle()}
            {renderBody()}
        </div>
    )
}

export default Notification
