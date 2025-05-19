import Https from "../utils/https.ts";

export default async (username: string, password: string) => {
	return await Https.post("/api/login", false, {
		username: username,
		password: password,
	})
}
