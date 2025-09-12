import {createFileRoute} from '@tanstack/react-router'
import PageHeader from "../../lib/components/PageHeader.tsx";
import {IconNote} from "@tabler/icons-react";
import PageWrapper from "../../lib/components/PageWrapper.tsx";
import {useQuery} from '@tanstack/react-query'
import getNote from '../../lib/api/note/get.ts'
import Note from "../../lib/components/Note.tsx";
import Loading from "../../lib/components/Loading.tsx";
import Error from "../../lib/components/Error.tsx";

export const Route = createFileRoute('/note/$noteId')({
    component: RouteComponent,
})

function RouteComponent() {
    const {noteId} = Route.useParams()

    const {isPending, isFetching, error, data, refetch} = useQuery({
        queryKey: ['note_' + noteId],
        queryFn: () => getNote(noteId)
    });

    return (
        <>
            <PageHeader
                icon={<IconNote size={18}/>}
                title={`Note ${data ? ("by " + (data?.user?.displayName ?? data?.user?.username)) : ""}`}
            />
            <PageWrapper padding={"timeline"} center={false}>
                {isPending || isFetching ? (
                    <Loading fill/>
                ) : error ? (
                    <Error error={error} retry={refetch}/>
                ) : (
                    <Note data={data}/>
                )}
            </PageWrapper>
        </>
    )
}
