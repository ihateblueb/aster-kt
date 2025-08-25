import Https from "../utils/https.ts";

export default async function getTimeline(type: string) {
    return await Https.get("/api/timeline/" + type, true)
}
