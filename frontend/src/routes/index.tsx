import { createFileRoute } from '@tanstack/react-router'
import PageWrapper from "../lib/components/PageWrapper.tsx";
import PageHeader from "../lib/components/PageHeader.tsx";
import {IconHome} from "@tabler/icons-react";
import localstore from "../lib/utils/localstore.ts";

export const Route = createFileRoute('/')({
	component: RouteComponent,
})

function RouteComponent() {
	const token = localstore.getParsed('token');

	if (token) {
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
	} else {
		return (
			<>
				<p>helo this is aster and its work in progress and the frontend isnt perfect and this unauthenticated view is super ultra low priority</p>
			</>
		)
	}
}
