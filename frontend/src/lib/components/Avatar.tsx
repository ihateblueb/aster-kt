import * as React from "react";
import './Avatar.scss'

function Avatar(
	{ user, size }:
	{ user: any, size?: undefined | 'big' | 'small' }
) {
	React.useEffect(() => {})

	return (
		<div className={`avatarCtn`}>
			<a href={`/@${user?.username}${user?.host ? ("@" + user?.host) : ""}`} className={`avatar  ${size ?? ""} highlightable`}>
				<img src={user?.avatar} alt={user?.avatarAlt ?? `${user.username}'s avatar`} />
			</a>
		</div>
	)
}

export default Avatar
