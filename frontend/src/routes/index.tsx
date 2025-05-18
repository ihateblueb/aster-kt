import { createFileRoute } from '@tanstack/react-router'
import PageWrapper from "../lib/components/PageWrapper.tsx";
import PageHeader from "../lib/components/PageHeader.tsx";
import {IconHome} from "@tabler/icons-react";

export const Route = createFileRoute('/')({
	component: RouteComponent,
})

function RouteComponent() {
	return (
		<>
			<PageHeader
				icon={<IconHome size={18} />}
				title={"test"}
			>
				tes
			</PageHeader>
			<PageWrapper padding={"full"} center={false}>
				<div>Hello from the PageWrapperComponent!</div>
			</PageWrapper>
		</>
	)
}
