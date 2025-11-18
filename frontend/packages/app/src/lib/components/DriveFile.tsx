import * as common from "aster-common";
import './DriveFile.scss'
import Container from "./Container.tsx";

function DriveFile({data}: { data: common.DriveFile }) {
    return (
        <div className={"driveFile"}>
            <img src={data.src} alt={data.alt}/>
            <p>{data.type}</p>
            <Container gap={"md"} align={"horizontal"}>
                Edit
                Delete
            </Container>
        </div>
    )
}

export default DriveFile;