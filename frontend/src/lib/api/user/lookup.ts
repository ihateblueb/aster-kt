import Https from "../../utils/https.ts";

export default async (handle: string) => {
    return await Https.get(`/api/lookup/${handle}`, true)
}
