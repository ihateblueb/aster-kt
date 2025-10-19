import {createRootRoute, Outlet} from '@tanstack/react-router';
import {QueryClientProvider} from "@tanstack/react-query";
import queryClient from "../lib/queryClient.ts";
import Sidebar from "../lib/components/Sidebar.tsx";

export const Route = createRootRoute({
    component: RootComponent,
})

function RootComponent() {
    return (
        <>
            <QueryClientProvider client={queryClient}>
                <Sidebar left/>
                <main>
                    <Outlet/>
                </main>
                <Sidebar right/>
            </QueryClientProvider>
        </>
    )
}
