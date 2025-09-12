import PageHeader from "../PageHeader.tsx";
import PageWrapper from "../PageWrapper.tsx";
import Error from "../Error.tsx";
import {IconAlertTriangle} from "@tabler/icons-react";
import ApiError from "../../utils/ApiError.ts";

function NotFoundPage() {
    return (
        <>
            <PageHeader icon={<IconAlertTriangle size={18}/>} title={"Something went wrong"}/>
            <PageWrapper padding={"full"} center={true}>
                <Error error={new ApiError(0, "Not found", "")}/>
            </PageWrapper>
        </>
    )
}

export default NotFoundPage
