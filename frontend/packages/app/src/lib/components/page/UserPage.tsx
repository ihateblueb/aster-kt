import "./UserPage.scss";
import PageHeader from "../PageHeader.tsx";
import PageWrapper from "../PageWrapper.tsx";
import {IconCake, IconDots, IconUser} from "@tabler/icons-react";
import {useQuery} from "@tanstack/react-query";
import lookup from "../../api/user/lookup.ts";
import Loading from "../Loading.tsx";
import Error from "../Error.tsx";
import Avatar from "../Avatar.tsx";
import {useState} from "react";
import Container from "../Container.tsx";
import Button from "../Button.tsx";
import Mfm from "../Mfm.tsx";

function UserPage(
    {handle}: { handle: string }
) {
    let [displayName, setDisplayName] = useState(handle);

    const {isLoading, isError, error, data} = useQuery({
        queryKey: [`user_${handle}`],
        queryFn: () => lookup(handle).then((e) => {
            setDisplayName(e?.displayName ?? e?.username ?? handle);
            return e
        }),
    });

    function render() {
        return (
            <>
                <Container gap={"lg"}>
                    <div className={"userHeader"} style={{backgroundImage: `url(${data?.banner})`}}></div>
                    <div className={"userIdentity"}>
                        <Container gap={"xl"} align={"horizontal"}>
                            <Container align={"horizontal"} gap={"md"}>
                                <Container>
                                    <Avatar size={"xl"} user={data}/>
                                </Container>
                                <Container gap={"sm"}>
                                    <span className={"displayName"}>{displayName}</span>
                                    <span
                                        className={"username"}>@{data?.username}{(data?.host === null) ? "" : `@${data?.host}`}</span>
                                </Container>
                            </Container>

                            <Button>Follow</Button>
                            <Button><IconDots size={18}/></Button>
                        </Container>
                    </div>
                    <div className={"underHeader"}>
                        <Container align={"left"} gap={"md"}>
                            <span
                                className={"bio" + ((data?.bio === undefined || data?.bio === "") ? " none" : "")}
                            >
                                {(data?.bio === undefined || data?.bio === "") ? "This user hasn't written a description yet." : (
                                    <Mfm text={data.bio}></Mfm>
                                )}
                            </span>

                            {data?.birthday != null ? (
                                <Container align={"horizontal"} gap={"md"}>
                                    <IconCake size={18}/>
                                    <span className={"birthday"}>Birthday!</span>
                                </Container>
                            ) : null}

                            {data?.createdAt != null ? (
                                <span className={"createdAt"}>Joined on {new Date(
                                    data.createdAt
                                ).toLocaleString(undefined, {
                                    month: 'long',
                                    day: 'numeric',
                                    year: 'numeric'
                                })}</span>
                            ) : null}
                        </Container>
                    </div>
                </Container>
            </>
        )
    }

    return (
        <div className={"userPage"}>
            <PageHeader icon={(data === undefined) ? <IconUser size={18}/> : <Avatar size={"sm"} user={data}/>}
                        title={displayName}/>
            <PageWrapper padding={"full"} center={false}>
                {isLoading ? (
                    <Loading fill={true}/>
                ) : isError ? (<Error error={error}/>) : render()}
            </PageWrapper>
        </div>
    )
}

export default UserPage
