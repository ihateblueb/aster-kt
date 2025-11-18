import Https from "../../utils/https.ts";
import {Note} from "aster-common";

export default async function repeatNote(id: string) {
    return await Https.post("/api/note/" + id + "/repeat", true, {
        content: null,
        visibility: "public" // TODO: Visibility Setting
    }) as Note | undefined;
}
