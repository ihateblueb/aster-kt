import * as React from "react";
import './Note.scss'
import Container from "./Container.tsx";
import Avatar from "./Avatar.tsx";
import {IconAlertTriangle, IconArrowBackUp, IconDots, IconPlus, IconRepeat, IconStar} from "@tabler/icons-react";
import DateTime from "./DateTime.tsx";
import Visibility from "./Visibility.tsx";
import Button from "./Button.tsx";

function Note(
    {data}:
    { data: any }
) {
    let [cwOpen, setCwOpen] = React.useState(false)

    function renderHandle() {
        return (
            <span className={"username"}>@{data?.user?.username}{data?.user?.host !== null ? (
                <span className={"host"}>@{data?.user?.host}</span>) : null}</span>
        )
    }

    function renderAt() {
        return `@${data?.user?.username}${data?.user?.host ? ("@" + data?.user?.host) : ""}`
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
                        <span>{data?.cw}</span>
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
        if (!data?.cw) {
            return (
                <Container>
                    <p>{data?.content}</p>
                </Container>
            )
        } else {
            return (
                <Container gap={"md"}>
                    {!cwOpen ? renderContentWarning() : (
                        <>
                            {renderContentWarning()}
                            <p>{data?.content}</p>
                        </>
                    )}
                </Container>
            )
        }
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

            {renderContent()}

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
