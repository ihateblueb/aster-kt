import * as React from "react";
import * as Common from "aster-common";
import './Note.scss'
import Container from "./Container.tsx";
import Avatar from "./Avatar.tsx";
import {IconAlertTriangle, IconArrowBackUp, IconDots, IconPlus, IconRepeat, IconStar} from "@tabler/icons-react";
import DateTime from "./DateTime.tsx";
import Visibility from "./Visibility.tsx";
import Button from "./Button.tsx";
import {Link} from "@tanstack/react-router";
import localstore from "../utils/localstore.ts";
import likeNote from "../api/note/like.ts";

function Note(
    {data}:
    { data: Common.Note }
) {
    let [note, setNote] = React.useState(data)

    let self = localstore.getSelf()
    let isOwnPost = note?.user?.id === self?.id

    let [cwOpen, setCwOpen] = React.useState(false)

    /* Interactions */

    function like() {
        likeNote(note?.id).then((e) => {
            if (e?.id === note?.id) setNote(e)
        })
    }

    /* Rendering */

    function renderHandle() {
        return (
            <span className={"username"}>@{note?.user?.username}{note?.user?.host !== null ? (
                <span className={"host"}>@{note?.user?.host}</span>) : null}</span>
        )
    }

    function renderAt() {
        return `@${note?.user?.username}${note?.user?.host ? ("@" + note?.user?.host) : ""}`
    }

    function renderContentWarning() {
        // todo: screenreader check
        return (
            <div className={"cw"}>
                <Container gap={"md"} align={"horizontal"}>
                    <Container>
                        <IconAlertTriangle size={18}/>
                    </Container>
                    <Container>
                        <span>{note?.cw}</span>
                    </Container>
                    <Container align={"right"}>
                        {!cwOpen ? (
                            <Button thin onClick={() => setCwOpen(true)}>Open</Button>
                        ) : (
                            <Button thin onClick={() => setCwOpen(false)}>Close</Button>
                        )}
                    </Container>
                </Container>
            </div>
        )
    }

    function renderContent() {
        // todo: screenreader check
        if (!note?.cw) {
            return (
                <Container>
                    <p>{note?.content}</p>
                </Container>
            )
        } else {
            return (
                <Container gap={"md"}>
                    {!cwOpen ? renderContentWarning() : (
                        <>
                            {renderContentWarning()}
                            <p>{note?.content}</p>
                        </>
                    )}
                </Container>
            )
        }
    }

    function renderTags() {
        if (!note.tags || note.tags.length === 0) return null

        let tags: React.JSX.Element[] = []
        for (const tag of note.tags) {
            tags.push(
                <div className={"tag " + tag}>
                    <Link to={"/tag/" + tag}>
                        #{tag}
                    </Link>
                </div>
            )
        }

        return (
            <Container align={"horizontal"} gap={"sm"}>
                {tags}
            </Container>
        )
    }

    return (
        <article className={"note highlightable"} tabIndex={0}
                 aria-label={`Note by ${renderAt()}${note?.content ? ", " + note?.content : ""}`}>
            <Container align={"horizontal"} gap={"lg"}>
                <Container>
                    <Avatar user={note?.user}/>
                </Container>
                <Container align={"left"}>
                    <a className={"names"}
                       href={`/${renderAt()}`}>
                        <p className={"displayName"}>{note?.user?.displayName ?? note?.user?.username}</p>
                        <p className={"handle"}>{renderHandle()}</p>
                    </a>
                </Container>
                <Container align={"right"}>
                    <Container gap={"sm"}>
                        <DateTime date={note?.createdAt} short={true}/>
                        <Visibility visibility={note?.visibility}/>
                    </Container>
                </Container>
            </Container>

            {renderContent()}

            {renderTags()}

            <footer>
                <button className={"highlightable"} title={"Reply"}>
                    <IconArrowBackUp aria-hidden={true} size={20}/>
                </button>
                <button
                    className={"highlightable" + ((note?.repeats?.some((e) => e?.id === self?.id)) ? " repeated" : "")}
                    title={"Repeat"}>
                    <IconRepeat aria-hidden={true} size={20}/>
                    {(note?.repeats?.length >= 1) ? <span>{note?.repeats?.length}</span> : null}
                </button>
                <button
                    className={"highlightable" + ((note?.likes?.some((e) => e?.id === self?.id)) ? " liked" : "")}
                    title={"Like"}
                    onClick={() => like()}
                >
                    <IconStar aria-hidden={true} size={20}/>
                    {(note?.likes?.length >= 1) ? <span>{note?.likes?.length}</span> : null}
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
