import Avatar from "../Avatar.tsx";
import localstore from "../../utils/localstore.ts";
import Container from "../Container.tsx";
import Button from "../Button.tsx";
import './Account.scss'

function AccountWidget() {
    let account = localstore.getParsed("self")

    if (account === undefined)
        return (
            <div className={"widget-account widget padded"}>
                <Container align={"horizontal"} gap={"md"}>
                    <Button wide center to={"/login"}>Login</Button>
                    <Button wide center to={"/register"}>Register</Button>
                </Container>
            </div>
        )

    return (
        <div className={`widget-account widget padded`}>
            <Avatar user={account}/>
            <div className={`names`}>
                <span>{account?.displayName ?? account?.username}</span>
                <span>@{account?.username}</span>
            </div>
        </div>
    )
}

export default AccountWidget;