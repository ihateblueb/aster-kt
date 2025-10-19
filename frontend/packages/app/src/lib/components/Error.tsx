import './Error.scss'
import Button from "./Button.tsx";
import {IconBug, IconReload} from "@tabler/icons-react";
import ApiError from "../utils/ApiError.ts";

function Error(
    {error, retry}: { error: ApiError | Error, retry?: () => {} }
) {
    return (
        <div className={"error"}>
            <span className={"title"}>Something went wrong</span>
            <span className={"msg"}>{error?.message}</span>
            <details>
                <summary>Detailed Information</summary>
                <div className={"stack"}>
                    {error?.message}
                    {(error instanceof ApiError && error.serverStackTrace !== "") ? (
                        <>
                            <p className={"stackHeader"}>Server<br/></p>
                            {error?.serverStackTrace}
                        </>
                    ) : null}
                    <p className={"stackHeader"}>Client<br/></p>
                    {error?.stack}
                </div>
            </details>
            <div className={"buttons"}>
                <Button onClick={() => {
                    if (retry) retry()
                }}>
                    <IconReload size={18}/>
                    Retry
                </Button>
                <Button>
                    <IconBug size={18}/>
                    Report Bug
                </Button>
            </div>
        </div>
    )
}

export default Error
