import {createFileRoute} from '@tanstack/react-router'
import PageWrapper from "../lib/components/PageWrapper.tsx";
import PageHeader from "../lib/components/PageHeader.tsx";
import {IconChartBubble, IconHome, IconPlanet, IconUsers} from "@tabler/icons-react";
import localstore from "../lib/utils/localstore.ts";
import Timeline from "../lib/components/Timeline.tsx";
import Note from "../lib/components/Note.tsx";
import {useQuery} from "@tanstack/react-query";
import getTimeline from "../lib/api/timeline.ts";
import {useState} from "react";
import Tab from "../lib/components/Tab.tsx";

export const Route = createFileRoute('/')({
    component: RouteComponent,
})

function RouteComponent() {
    const token = localstore.getParsed('token');

    if (token) {
        let previousTimeline = localstore.getParsed('timeline')
        let [timeline, setTimeline] = useState((previousTimeline === undefined) ? "home" : previousTimeline);

        const {isPending, error, data, isFetching, refetch} = useQuery({
            queryKey: ['Timeline'],
            queryFn: async () => await getTimeline(timeline),
        })

        function updateTimeline(timeline: string) {
            setTimeline(timeline);
            localstore.set('timeline', timeline);
            setTimeout(async () => {
                await refetch()
            }, 100)
        }

        return (
            <>
                <PageHeader
                    icon={<IconHome size={18}/>}
                    title={"Timeline"}
                >
                    <Tab onClick={() => updateTimeline("home")} selected={timeline === "home"}
                         aria-label={"Home timeline"}>
                        <IconHome size={18}/>
                    </Tab>
                    <Tab onClick={() => updateTimeline("local")} selected={timeline === "local"}
                         aria-label={"Local timeline"}>
                        <IconUsers size={18}/>
                    </Tab>
                    <Tab onClick={() => updateTimeline("bubble")} selected={timeline === "bubble"}
                         aria-label={"Bubble timeline"}>
                        <IconChartBubble size={18}/>
                    </Tab>
                    <Tab onClick={() => updateTimeline("public")} selected={timeline === "public"}
                         aria-label={"Public timeline"}>
                        <IconPlanet size={18}/>
                    </Tab>
                </PageHeader>
                <PageWrapper padding={"full"} center={false}>
                    {isPending || isFetching ? (
                        <p>Loading...</p>
                    ) : error ? (
                        <p>Errored</p>
                    ) : (
                        <Timeline data={data} Component={Note}/>
                    )}
                </PageWrapper>
            </>
        )
    } else {
        return (
            <>
                <p>helo this is aster and its work in progress and the frontend isnt perfect and this unauthenticated
                    view is super ultra low priority</p>
            </>
        )
    }
}
