import Https from "../../utils/https.ts";
import {DriveFile} from "aster-common";

export default async () => {
    return await Https.get(`/api/drive`, true) as DriveFile[] | undefined;
}
