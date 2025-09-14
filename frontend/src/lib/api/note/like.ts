import Https from "../../utils/https.ts";

export default async function likeNote(id: string) {
    return await Https.post("/api/note/" + id + "/like", true)
}
