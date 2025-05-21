import { Outlet, createRootRoute } from '@tanstack/react-router';
import {QueryClientProvider} from "@tanstack/react-query";
import queryClient from '../lib/queryClient.ts'

export const Route = createRootRoute({
	component: RootComponent,
})

function RootComponent() {
	return (
		<>
			<QueryClientProvider client={queryClient}>
				<main>
					<Outlet />
				</main>
			</QueryClientProvider>
		</>
	)
}
