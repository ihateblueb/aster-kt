import * as React from "react";
import {IconX} from "@tabler/icons-react";
import './Info.scss'

function Info(
	{ type, icon, dismissable, children }:
	{ type?: undefined | "primary" | "success" | "warn" | "danger", icon: React.ReactNode, dismissable: boolean, children?: React.ReactNode }
) {
	let [shown, setShown] = React.useState(true)

	if (shown)
		return (
			<div className={`info ${type}`}>
				{icon ?
					<div className={"icon"}>
						{icon}
					</div>
					: null}

				<div className={"content"}>
					{children}
				</div>

				{dismissable ?
					<button className={"dismiss"} onClick={() => setShown(false)}>
						<IconX size={18} />
					</button>
					: null}
			</div>
		)
}

export default Info
