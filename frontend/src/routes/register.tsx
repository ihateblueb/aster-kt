import { createFileRoute } from '@tanstack/react-router'
import PageHeader from "../lib/components/PageHeader.tsx";
import {IconUserPlus} from "@tabler/icons-react";
import PageWrapper from "../lib/components/PageWrapper.tsx";
import {useForm} from "@tanstack/react-form";
import Input from "../lib/components/Input.tsx";
import Container from "../lib/components/Container.tsx";
import Button from "../lib/components/Button.tsx";
import Info from '../lib/components/Info.tsx';
import login from "../lib/api/login.ts"
import localstore from "../lib/utils/localstore.ts";
import router from "../lib/router.ts";
import {useQuery} from "@tanstack/react-query";
import getMeta from "../lib/api/meta/get.ts";

export const Route = createFileRoute('/register')({
	component: RouteComponent,
	onEnter: async () => {
		const token = localstore.getParsed('token');

		if (token)
			router.navigate({
				href: "/"
			});

		/*
		* Require invite to be input first, then redirect to /register?invite=code
		* This will then show the user/pass section & allows for admins & mods to send a direct frontend link for easier registration
		* */
	}
})

function RouteComponent() {
	const { isLoading, isError, error, data } = useQuery({
		queryKey: ['meta'],
		queryFn: () => getMeta()
	});

	const form = useForm({
		defaultValues: {
			invite: '',
			username: '',
			password: ''
		},
		onSubmit: async (values) => {
			console.log(values);
			return;

			// todo:
			await login(values.value.username, values.value.password).then((result) => {
				if (result.token && result.user) {
					localstore.set("token", result.token);
					localstore.set("self", JSON.stringify(result.user));

					router.navigate({
						href: "/"
					});
				}
			});
		}
	})

	return (
		<>
			<PageHeader
				icon={<IconUserPlus size={18} />}
				title={"Register"}
			/>
			<PageWrapper padding={"full"} center={true}>
				<form
					onSubmit={async (e) => {
						e.preventDefault();
						e.stopPropagation();
						await form.handleSubmit();
					}}
				>
					{isLoading ? <p>Loading</p> : isError ? <p>Error: {error.message}</p> : (
						<Container gap={"md"} align={"center"}>
							<form.Field
								name={"username"}
								validators={{
									onChange: ({ value }) =>
										!value
											? "Username required"
											: (value.length < 1)
												? "Username must be at least 1 character"
												: undefined,
									onChangeAsyncDebounceMs: 500
								}}
								children={(field) => {
									return (
										<>
											<Input
												id={field.name}
												name={field.name}
												placeholder={"Username"}
												type={"username"}
												value={field.state.value}
												onBlur={field.handleBlur}
												onChange={(e) => field.handleChange(e.target.value)}
											/>
											{!field.state.meta.isValid && (
												<Info text={true} type={"danger"}>{field.state.meta.errors}</Info>
											)}
										</>
									)
								}}
							/>
							<form.Field
								name={"password"}
								validators={{
									onChange: ({ value }) =>
										!value
											? "Password required"
											: (value.length < 6)
												? "Password must be at least 6 characters"
												: undefined,
									onChangeAsyncDebounceMs: 500
								}}
								children={(field) => {
									return (
										<>
											<Input
												id={field.name}
												name={field.name}
												placeholder={"Password"}
												type={"password"}
												value={field.state.value}
												onBlur={field.handleBlur}
												onChange={(e) => field.handleChange(e.target.value)}
											/>
											{!field.state.meta.isValid && (
												<Info text={true} type={"danger"}>{field.state.meta.errors}</Info>
											)}
										</>
									)
								}}
							/>

							<Container align={"horizontal"}>
								<Container align={"left"}>
									<a href="/forgot-password">
										Forgot password?
									</a>
								</Container>

								<Container align={"right"}>
									<form.Subscribe
										selector={(state) => [state.canSubmit, state.isSubmitting]}
										children={([canSubmit, isSubmitting]) => (
											<Button type="submit" disabled={!canSubmit}>
												{isSubmitting ? '...' : 'Register'}
											</Button>
										)}
									/>
								</Container>
							</Container>
						</Container>
					)}
				</form>
			</PageWrapper>
		</>
	)
}
