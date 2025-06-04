import Https from "../../utils/https.ts";

export default async function getMeta() {
	return await Https.get("/api/meta", false)
}
