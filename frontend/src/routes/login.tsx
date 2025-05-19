import { createFileRoute } from '@tanstack/react-router'
import PageHeader from "../lib/components/PageHeader.tsx";
import {IconLogin} from "@tabler/icons-react";
import PageWrapper from "../lib/components/PageWrapper.tsx";
import {useForm} from "@tanstack/react-form";
import Input from "../lib/components/Input.tsx";
import Container from "../lib/components/Container.tsx";
import Button from "../lib/components/Button.tsx";

export const Route = createFileRoute('/login')({
	component: RouteComponent,
})

function RouteComponent() {
	const form = useForm({
		defaultValues: {
			username: '',
			password: ''
		},
		onSubmit: async (values) => {
			console.log(values);
		}
	})

	return (
		<>
			<PageHeader
				icon={<IconLogin size={18} />}
				title={"Login"}
			/>
			<PageWrapper padding={"full"} center={true}>
				<form
					onSubmit={async (e) => {
						e.preventDefault();
						e.stopPropagation();
						await form.handleSubmit();
					}}
				>
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
									<Input
										id={field.name}
										name={field.name}
										placeholder={"Username"}
										type={"username"}
										value={field.state.value}
										onBlur={field.handleBlur}
										onChange={(e) => field.handleChange(e.target.value)}
									/>
								)
							}}
						/>
						<form.Field
							name={"password"}
							validators={{
								onChange: ({ value }) =>
									!value
										? "Password required"
										: (value.length < 1)
											? "Password must be at least 6 character"
											: undefined,
								onChangeAsyncDebounceMs: 500
							}}
							children={(field) => {
								return (
									<Input
										id={field.name}
										name={field.name}
										placeholder={"Password"}
										type={"password"}
										value={field.state.value}
										onBlur={field.handleBlur}
										onChange={(e) => field.handleChange(e.target.value)}
									/>
								)
							}}
						/>

						<Container align={"right"}>
							<form.Subscribe
								selector={(state) => [state.canSubmit, state.isSubmitting]}
								children={([canSubmit, isSubmitting]) => (
									<Button type="submit" disabled={!canSubmit}>
										{isSubmitting ? '...' : 'Login'}
									</Button>
								)}
							/>
						</Container>
					</Container>
				</form>
			</PageWrapper>
		</>
	)
}
