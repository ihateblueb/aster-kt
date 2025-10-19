import Https from "../../utils/https.ts";
import {Note} from "aster-common";

export default async function getNote(id: string) {
    return await Https.get("/api/note/" + id, true) as Note | undefined;
}
