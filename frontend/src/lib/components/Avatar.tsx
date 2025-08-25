import * as React from "react";
import './Avatar.scss'

function Avatar(
    {user, size}:
    { user: any, size?: undefined | 'big' | 'small' }
) {
    React.useEffect(() => {
    })

    let fallback = "/img/avatar.png"

    return (
        <div className={`avatarCtn`}>
            <a href={`/@${user?.username}${user?.host ? ("@" + user?.host) : ""}`}
               className={`avatar ${size ?? ""} highlightable`}>
                <img src={user?.avatar ?? fallback} alt={user?.avatarAlt ?? `${user.username}'s avatar`}
                     onError={e => e.currentTarget.src = fallback}/>
            </a>
        </div>
    )
}

export default Avatar
