import {createFileRoute} from '@tanstack/react-router'
import localstore from "../lib/utils/localstore.ts";

export const Route = createFileRoute('/logout')({
    component: RouteComponent,
    onEnter: async () => {
        localstore.delete('token')
        document.cookie = `AsAuthorization=; expires=Thu, 01 Jan 1970 00:00:00 UTC`;
        localstore.delete('self')

        window.location.replace("/");
    }
})

function RouteComponent() {
    return
}
