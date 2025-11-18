import * as common from "aster-common";
import './DriveFile.scss'
import Container from "./Container.tsx";
import Button from "./Button.tsx";
import {IconPencil, IconTrash} from "@tabler/icons-react";

function DriveFile({data}: { data: common.DriveFile }) {
    return (
        <div className={"driveFile"}>
            <img src={data.src} alt={data.alt}/>
            <Container gap={"md"} align={"horizontalCenter"}>
                <Button onClick={() => alert("TODO: Edit")}>
                    <IconPencil size={18}/>
                </Button>
                <Button danger onClick={() => alert("TODO: Delete")}>
                    <IconTrash size={18}/>
                </Button>
            </Container>
        </div>
    )
}

export default DriveFile;
