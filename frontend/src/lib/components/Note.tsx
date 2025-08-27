import * as React from "react";
import './Note.scss'
import Container from "./Container.tsx";
import Avatar from "./Avatar.tsx";
import {IconArrowBackUp, IconDots, IconPlus, IconRepeat, IconStar} from "@tabler/icons-react";
import DateTime from "./DateTime.tsx";
import Visibility from "./Visibility.tsx";

function Note(
    {data}:
    { data: any }
) {
    React.useEffect(() => {
    })

    function renderHandle() {
        return (
            <span className={"username"}>@{data?.user?.username}{data?.user?.host !== null ? (
                <span className={"host"}>@{data?.user?.host}</span>) : null}</span>
        )
    }

    function renderAt() {
        return `@${data?.user?.username}${data?.user?.host ? ("@" + data?.user?.host) : ""}`
    }

    return (
        <article className={"note highlightable"} tabIndex={0}
                 aria-label={`Note by @${renderAt()}${data?.content ? ", " + data?.content : ""}`}>
            <Container align={"horizontal"} gap={"lg"}>
                <Container>
                    <Avatar user={data?.user}/>
                </Container>
                <Container align={"left"}>
                    <a className={"names"}
                       href={`/${renderAt()}`}>
                        <p className={"displayName"}>{data?.user?.displayName ?? data?.user?.username}</p>
                        <p className={"handle"}>{renderHandle()}</p>
                    </a>
                </Container>
                <Container align={"right"}>
                    <Container gap={"sm"}>
                        <DateTime date={data?.createdAt} short={true}/>
                        <Visibility visibility={data?.visibility}/>
                    </Container>
                </Container>
            </Container>

            <Container>
                <p>{data?.content}</p>
            </Container>

            <footer>
                <button className={"highlightable"} title={"Reply"}>
                    <IconArrowBackUp aria-hidden={true} size={20}/>
                </button>
                <button className={"highlightable"} title={"Repeat"}>
                    <IconRepeat aria-hidden={true} size={20}/>
                </button>
                <button className={"highlightable"} title={"Like"}>
                    <IconStar aria-hidden={true} size={20}/>
                </button>
                <button className={"highlightable"} title={"React"}>
                    <IconPlus aria-hidden={true} size={20}/>
                </button>
                <button className={"highlightable"} title={"More"}>
                    <IconDots aria-hidden={true} size={20}/>
                </button>
            </footer>
        </article>
    )
}

export default Note
