import Https from "../utils/https.ts";

export default async (username: string, password: string) => {
	return await Https.post("/api/login", {
		username: username,
		password: password,
	})
}
