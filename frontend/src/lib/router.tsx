import {createRouter} from "@tanstack/react-router";
import {routeTree} from "../routeTree.gen.ts";
import PageHeader from "./components/PageHeader.tsx";
import {IconExclamationCircle} from "@tabler/icons-react";
import PageWrapper from "./components/PageWrapper.tsx";
import Button from "./components/Button.tsx";

const router = createRouter({
    routeTree,
    defaultNotFoundComponent: () => {
        return (
            <>
                <PageHeader icon={<IconExclamationCircle size={18}/>} title={"Something went wrong"}/>
                <PageWrapper padding={"full"} center={true}>
                    <h1>Not found</h1>
                    <Button onClick={() => window.location.reload()}>Retry</Button>
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
