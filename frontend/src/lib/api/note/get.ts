import Https from "../../utils/https.ts";

export default async function getNote(id: string) {
	return await Https.get("/api/note/"+id, true)
}
