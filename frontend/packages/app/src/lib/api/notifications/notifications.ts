import Https from "../../utils/https.ts";
import {Notification} from "aster-common";

export default async function getNotifications() {
    return await Https.get("/api/notifications", true) ?? [] as Notification[];
}
