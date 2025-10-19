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
import Loading from "../lib/components/Loading.tsx";
import Error from "../lib/components/Error.tsx";

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

        let selectedIcon;
        switch (timeline) {
            case "home":
                selectedIcon = <IconHome size={18}/>
                break;
            case "local":
                selectedIcon = <IconUsers size={18}/>
                break;
            case "bubble":
                selectedIcon = <IconChartBubble size={18}/>
                break;
            case "public":
                selectedIcon = <IconPlanet size={18}/>
                break;
        }

        return (
            <>
                <PageHeader
                    icon={selectedIcon}
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
                        <Loading fill/>
                    ) : error ? (
                        <Error error={error} retry={refetch}/>
                    ) : (
                        <Timeline data={data} Component={Note}/>
                    )}
                </PageWrapper>
            </>
        )
    } else {
        return (
            <>
                <h1>Aster-Kt</h1>
                <p>
                    This is heavily in development software.<br/>
                    If you have an account, you can <a href={"/login"}>log in</a>.
                </p>
            </>
        )
    }
}
