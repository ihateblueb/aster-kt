import Https from "../utils/https.ts";
import {Note} from "aster-common";

export default async function getTimeline(type: string) {
    return await Https.get("/api/timeline/" + type, true) as Array<Note> | undefined;
}
