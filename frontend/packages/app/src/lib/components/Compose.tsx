import './Compose.scss'
import Container from "./Container.tsx";
import localstore from "../utils/localstore.ts";
import Avatar from "./Avatar.tsx";
import {IconAlertTriangle, IconMoodSmile, IconPaperclip, IconWorld} from "@tabler/icons-react";
import Button from "./Button.tsx";
import TextArea from "./TextArea.tsx";

function Compose() {
    const placeholders = [
        "What's on your mind?",
        "What are you thinking about?",
        "Hows your day going?"
    ]

    let account = localstore.getParsed("self")

    if (account === undefined) return <></>

    function renderHeader() {
        return (
            <Container align={"horizontal"}>
                <Container align={"left"}>
                    <Avatar size={"md"} user={account}/>
                </Container>
                <Container align={"right"}>
                    <Button nav>
                        <IconWorld size={18}/>
                    </Button>
                </Container>
            </Container>
        )
    }

    function renderFooter() {
        return (
            <Container align={"horizontal"}>
                <Container align={"left"}>
                    <Container align={"horizontal"}>
                        <Button nav>
                            <IconPaperclip size={18}/>
                        </Button>
                        <Button nav>
                            <IconAlertTriangle size={18}/>
                        </Button>
                        <Button nav>
                            <IconMoodSmile size={18}/>
                        </Button>
                    </Container>
                </Container>
                <Container align={"right"}>
                    <Button>Post</Button>
                </Container>
            </Container>
        )
    }

    /*
    * Dropdown for IconPaperclip
    * Upload media
    * Use media from drive
    * Attach poll
    * */

    return (
        <div className={`compose`}>
            <Container gap={"md"}>
                {renderHeader()}
                <TextArea
                    wide
                    rows={5}
                    placeholder={placeholders[Math.floor(Math.random() * placeholders.length)]}
                />
                {renderFooter()}
            </Container>
        </div>
    )
}

export default Compose
