import { createFileRoute } from '@tanstack/react-router'
import localstore from "../lib/utils/localstore.ts";

export const Route = createFileRoute('/logout')({
  component: RouteComponent,
	onEnter: async () => {
		localstore.delete('token')
		localstore.delete('self')
	}
})

function RouteComponent() {
  return
}
