import Https from "../utils/https.ts";
import {AuthResponse} from "aster-common";

export default async (username: string, password: string, invite: string) => {
    return await Https.post("/api/register", false, {
        username: username,
        password: password,
        invite: invite,
    }) as AuthResponse | undefined;
}
