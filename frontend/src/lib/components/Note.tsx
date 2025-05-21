import * as React from "react";
import './Note.scss'
import Container from "./Container.tsx";
import Avatar from "./Avatar.tsx";
import Visibility from "./Visibility.tsx";
import {IconArrowBackUp, IconDots, IconPlus, IconRepeat, IconStar} from "@tabler/icons-react";

function Note(
	{ note }:
	{ note: any }
) {
	React.useEffect(() => {})

	function renderHandle() {
		return (
			<span className={"username"}>@{note?.user?.username}{note?.user?.host !== null ? (<span className={"host"}>@{note?.user?.host}</span>) : null}</span>
		)
	}

	return (
		<article className={"note highlightable"} tabIndex={0} aria-label={`Note by @${note?.user?.username}${note?.user?.host ? ("@" + note?.user?.host) : ""}`}>
			<Container align={"horizontal"} gap={"lg"}>
				<Container>
					<Avatar user={note?.user} />
				</Container>
				<Container align={"left"}>
					<p className={"displayName"}>{note?.user?.displayName ?? note?.user?.username}</p>
					<p className={"handle"}>{renderHandle()}</p>
				</Container>
				<Container align={"right"}>
					<p>{note?.createdAt}</p>
					<Visibility visibility={note?.visibility} />
				</Container>
			</Container>

			<Container>
				<p>{note?.content}</p>
			</Container>

			<footer>
				<button className={"highlightable"} title={"Reply"}>
					<IconArrowBackUp aria-hidden={true} size={20} />
				</button>
				<button className={"highlightable"} title={"Repeat"}>
					<IconRepeat aria-hidden={true} size={20} />
				</button>
				<button className={"highlightable"} title={"Like"}>
					<IconStar aria-hidden={true} size={20} />
				</button>
				<button className={"highlightable"} title={"React"}>
					<IconPlus aria-hidden={true} size={20} />
				</button>
				<button className={"highlightable"} title={"More"}>
					<IconDots aria-hidden={true} size={20} />
				</button>
			</footer>
		</article>
	)
}

export default Note
