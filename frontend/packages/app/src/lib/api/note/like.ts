import Https from "../../utils/https.ts";
import {Note} from "aster-common";

export default async function likeNote(id: string) {
    return await Https.post("/api/note/" + id + "/like", true) as Note | undefined;
}
