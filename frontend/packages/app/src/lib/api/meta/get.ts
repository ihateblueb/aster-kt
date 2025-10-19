import Https from "../../utils/https.ts";
import {Meta} from "aster-common";

export default async function getMeta() {
    return await Https.get("/api/meta", false) as Meta;
}
