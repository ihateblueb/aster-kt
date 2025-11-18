import Https from "../../utils/https.ts";

export default async function deleteNote(id: string) {
    return await Https.delete("/api/note/" + id);
}
