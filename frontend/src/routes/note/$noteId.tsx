import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/note/$noteId')({
	component: RouteComponent,
})

function RouteComponent() {
	const { noteId } = Route.useParams()

	return <div>Hello "/note/{noteId}"!</div>
}
