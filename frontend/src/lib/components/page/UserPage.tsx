import PageHeader from "../PageHeader.tsx";
import PageWrapper from "../PageWrapper.tsx";
import {IconAlertTriangle} from "@tabler/icons-react";

function UserPage(
    {handle}: { handle: string }
) {
    return (
        <>
            <PageHeader icon={<IconAlertTriangle size={18}/>} title={"User"}/>
            <PageWrapper padding={"full"} center={true}>
                <p>{handle}</p>
            </PageWrapper>
        </>
    )
}

export default UserPage
