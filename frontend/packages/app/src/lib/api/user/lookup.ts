import Https from "../../utils/https.ts";
import {User} from "aster-common";

export default async (handle: string) => {
    return await Https.get(`/api/lookup/${handle}`, true) as User | undefined;
}
