import "./Tab.scss"
import * as React from "react";

interface TabProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    selected?: boolean;
    children: React.ReactNode;
}

function Tab(
    {selected, children, ...props}: TabProps
) {
    return (
        <button className={`tab${selected ? " selected" : ""}`} {...props}>
            {children}
            <span className={`bar${selected ? " selected" : ""}`}></span>
        </button>
    )
}

export default Tab
