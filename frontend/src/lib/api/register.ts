import Https from "../utils/https.ts";

export default async (username: string, password: string, invite: string) => {
	return await Https.post("/api/register", false, {
		username: username,
		password: password,
		invite: invite,
	})
}
