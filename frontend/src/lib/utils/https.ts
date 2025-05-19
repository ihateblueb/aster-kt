import {useStore} from "@tanstack/react-store";
import {store} from "./store.ts";
import ApiError from "./ApiError.ts";
import localstore from "./localstore.ts";

class Https {
	private count(num: number) {
		const count: number = useStore(store, (state): number => state[count]);
		store.setState((state) => {
			return {
				...state,
				[count]: count + num,
			}
		})
	}

	private start() {
		this.count(1);
	}
	private async end(res: Response) {
		this.count(-1);

		let body;
		try {
			body = await res.json() ?? undefined;
		} catch { /* empty */ }

		if (!res.ok)
			throw new ApiError(
				res.status,
				body?.message ?? "Something went wrong"
			);

		console.log(body);

		return body;
	}

	public async get(url: string, auth?: boolean) {
		this.start()

		const req = await fetch(url, {
			method: 'GET',
			headers: auth
				? {
					Authorization: 'Bearer ' + localstore.getParsed('token')
				}
				: {}
		});

		return await this.end(req)
	}

	public async post(url: string, body?: any) {
		this.start()

		const req = await fetch(url, {
			method: 'POST',
			headers: {
				Authorization: 'Bearer ' + localstore.getParsed('token')
			},
			body: JSON.stringify(body)
		});

		return await this.end(req)
	}

	public async patch(url: string, body?: any) {
		this.start()

		const req = await fetch(url, {
			method: 'PATCH',
			headers: {
				Authorization: 'Bearer ' + localstore.getParsed('token')
			},
			body: JSON.stringify(body)
		});

		return await this.end(req)
	}

	public async delete(url: string) {
		this.start()

		const req = await fetch(url, {
			method: 'DELETE',
			headers: {
				Authorization: 'Bearer ' + localstore.getParsed('token')
			}
		});

		return await this.end(req)
	}
}

export default new Https();
