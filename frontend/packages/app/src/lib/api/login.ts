import Https from "../utils/https.ts";
import {AuthResponse} from "aster-common";

export default async (username: string, password: string) => {
    return await Https.post("/api/login", false, {
        username: username,
        password: password,
    }) as AuthResponse | undefined;
}
