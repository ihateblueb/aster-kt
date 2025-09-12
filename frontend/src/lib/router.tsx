import {createRouter} from "@tanstack/react-router";
import {routeTree} from "../routeTree.gen.ts";
import PageHeader from "./components/PageHeader.tsx";
import {IconAlertTriangle} from "@tabler/icons-react";
import PageWrapper from "./components/PageWrapper.tsx";
import Error from "./components/Error.tsx";
import ApiError from "./utils/ApiError.ts";

const router = createRouter({
    routeTree,
    defaultNotFoundComponent: () => {
        return (
            <>
                <PageHeader icon={<IconAlertTriangle size={18}/>} title={"Something went wrong"}/>
                <PageWrapper padding={"full"} center={true}>
                    <Error error={new ApiError(0, "Not found", "")}/>
                </PageWrapper>
            </>
        )
    }
})

declare module '@tanstack/react-router' {
    interface Register {
        router: typeof router
    }
}

export default router
