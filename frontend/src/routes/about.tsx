import {createFileRoute} from '@tanstack/react-router'
import {useQuery} from "@tanstack/react-query";
import getMeta from "../lib/api/meta/get.ts";
import PageHeader from "../lib/components/PageHeader.tsx";
import {IconInfoCircle} from "@tabler/icons-react";
import PageWrapper from '../lib/components/PageWrapper.tsx';

export const Route = createFileRoute('/about')({
	component: RouteComponent,
})

function RouteComponent() {
	const {isLoading, isError, error, data} = useQuery({
		queryKey: ['meta'],
		queryFn: () => getMeta(),
	});

	return (
		<>
			<PageHeader
				icon={<IconInfoCircle size={18}/>}
				title={"About"}
			/>
			<PageWrapper padding={"full"} center={true}>
				<b>Version</b>
				<p>
					{data?.version?.aster}<br/>
					(Kotlin {data?.version?.kotlin}, Runtime {data?.version?.java}, {data?.version?.system})
				</p>
			</PageWrapper>
		</>
	)
}
