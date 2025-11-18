import * as common from "aster-common";
import './DriveFile.scss'
import Container from "./Container.tsx";
import Button from "./Button.tsx";
import {IconPencil, IconTrash} from "@tabler/icons-react";

function DriveFile({data}: { data: common.DriveFile }) {
    return (
        <div className={"driveFile"}>
            <img src={data.src} alt={data.alt}/>
            <span>{data.type}</span>
            <Container gap={"md"} align={"horizontal"}>
                <Button>
                    <IconPencil size={18}/>
                </Button>
                <Button danger>
                    <IconTrash size={18}/>
                </Button>
            </Container>
        </div>
    )
}

export default DriveFile;
