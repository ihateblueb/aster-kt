import {createFileRoute} from "@tanstack/react-router";
import PageHeader from "../lib/components/PageHeader.tsx";
import {IconFolder} from "@tabler/icons-react";
import PageWrapper from "../lib/components/PageWrapper.tsx";
import {useQuery} from "@tanstack/react-query";
import localstore from "../lib/utils/localstore.ts";
import get from "../lib/api/drive/get.ts";

export const Route = createFileRoute('/drive')({
    component: RouteComponent,
})

function RouteComponent() {
    const {data} = useQuery({
        queryKey: [`drive_${localstore.getSelf()?.id}`],
        queryFn: () => get(),
    });

    return (
        <>
            <PageHeader
                icon={<IconFolder size={18}/>}
                title={"Drive"}
            />
            <PageWrapper padding={"full"} center={false}>
                {data?.map((file) => (
                    <p>{JSON.stringify(file)}</p>
                ))}
            </PageWrapper>
        </>
    )
}