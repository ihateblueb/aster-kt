import {createFileRoute} from '@tanstack/react-router'
import PageHeader from "../lib/components/PageHeader.tsx";
import {IconCube} from "@tabler/icons-react";
import PageWrapper from "../lib/components/PageWrapper.tsx";
import Note from "../lib/components/Note.tsx";

export const Route = createFileRoute('/styletest')({
    component: RouteComponent,
})

let falseNote = {
    "id": "abtxvwkebqzz00bq",
    "apId": "https://test.aster.pages.gay/notes/abtxvwkebqzz00bq",
    "conversation": null,
    "user": {
        "id": "a8m3o3nw2oxh0004",
        "apId": "https://test.aster.pages.gay/users/a8m3o3nw2oxh0004",
        "inbox": "https://test.aster.pages.gay/users/a8m3o3nw2oxh0004/inbox",
        "outbox": "https://test.aster.pages.gay/users/a8m3o3nw2oxh0004/outbox",
        "username": "wbreen",
        "host": null,
        "displayName": "Dr. Wallace Breen",
        "bio": null,
        "location": null,
        "birthday": null,
        "avatar": null,
        "avatarAlt": null,
        "banner": null,
        "bannerAlt": null,
        "locked": false,
        "suspended": false,
        "activated": true,
        "automated": false,
        "discoverable": false,
        "indexable": false,
        "sensitive": false,
        "isCat": false,
        "speakAsCat": false,
        "createdAt": "2025-06-04T18:45:19.662485",
        "updatedAt": null,
        "publicKey": "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAss4OV2PDFYSGh/U9qrBi\n7G0ymVc0N+7MTDTX7oDmE6D9S9c6C/xDa/s0uFeUjxsDSY/HNXiRPAp7TdWklaAH\nYCjCuDo/bCHJ00+7uMPvJWLRYurc8N6r87x+fS1fz/caSHQGhSRTpUCV6FvKKIaw\nwlLh6UwhrRjI8fqjPZGMfqCJSCa0inVORDbDHZn921RK/3eFyUgrzRQPnNAJCCDo\nS75j5FTtK40Zl4NNoON6vFjnZbpVLhC6aROVT880nb7IvycP7ZZLLL2J9/XHTB9t\nJfhdIp11htMJpF9XY9Uw3DDeT5/UDsH/nmo7xBYYV3fH/2lfrbjin0OXKpupSNaY\npQIDAQAB\n-----END PUBLIC KEY-----\n"
    },
    "replyingTo": null,
    "cw": "Horrible",
    "content": "Welcome. Welcome to City 17. You have chosen, or been chosen, to relocate to one of our finest remaining urban centers. I thought so much of City 17 that I elected to establish my Administration here, in the Citadel so thoughtfully provided by Our Benefactors. I have been proud to call City 17 my home. And so, whether you are here to stay, or passing through on your way to parts unknown, welcome to City 17.",
    "visibility": "public",
    "to": [],
    "tags": ["test", "awesome", "gay", "trans", "boosted"],
    "createdAt": "2025-08-24T20:24:41.630554",
    "updatedAt": null
}


let falseNote2 = {
    "id": "abtxvwkebqzz00b2",
    "apId": "https://test.aster.pages.gay/notes/abtxvwkebqzz00bq",
    "conversation": null,
    "user": {
        "id": "a8m3o3nw2oxh0003",
        "apId": "https://test.aster.pages.gay/users/a8m3o3nw2oxh0004",
        "inbox": "https://test.aster.pages.gay/users/a8m3o3nw2oxh0004/inbox",
        "outbox": "https://test.aster.pages.gay/users/a8m3o3nw2oxh0004/outbox",
        "username": "harper",
        "host": "remlit.site",
        "displayName": null,
        "bio": null,
        "location": null,
        "birthday": null,
        "avatar": null,
        "avatarAlt": null,
        "banner": null,
        "bannerAlt": null,
        "locked": false,
        "suspended": false,
        "activated": true,
        "automated": false,
        "discoverable": false,
        "indexable": false,
        "sensitive": false,
        "isCat": true,
        "speakAsCat": false,
        "createdAt": "2025-06-04T18:45:19.662485",
        "updatedAt": null,
        "publicKey": "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAss4OV2PDFYSGh/U9qrBi\n7G0ymVc0N+7MTDTX7oDmE6D9S9c6C/xDa/s0uFeUjxsDSY/HNXiRPAp7TdWklaAH\nYCjCuDo/bCHJ00+7uMPvJWLRYurc8N6r87x+fS1fz/caSHQGhSRTpUCV6FvKKIaw\nwlLh6UwhrRjI8fqjPZGMfqCJSCa0inVORDbDHZn921RK/3eFyUgrzRQPnNAJCCDo\nS75j5FTtK40Zl4NNoON6vFjnZbpVLhC6aROVT880nb7IvycP7ZZLLL2J9/XHTB9t\nJfhdIp11htMJpF9XY9Uw3DDeT5/UDsH/nmo7xBYYV3fH/2lfrbjin0OXKpupSNaY\npQIDAQAB\n-----END PUBLIC KEY-----\n"
    },
    "replyingTo": null,
    "cw": null,
    "content": "meow",
    "visibility": "unlisted",
    "to": [],
    "tags": [],
    "createdAt": "2025-08-24T20:24:41.630554",
    "updatedAt": null
}

function RouteComponent() {
    return <>
        <PageHeader icon={<IconCube size={18}/>} title={"Style Test"}></PageHeader>
        <PageWrapper padding={"timeline"} center={false}>
            <Note data={falseNote}/>
            <Note data={falseNote2}/>
            <Note data={falseNote}/>
        </PageWrapper>
    </>
}
