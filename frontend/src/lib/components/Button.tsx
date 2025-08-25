import './Button.scss'
import * as React from "react";
import {useNavigate} from "@tanstack/react-router";

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    wide?: boolean;
    thin?: boolean;
    nav?: boolean;
    to?: string;
    children: React.ReactNode;
}

function Button({wide, thin, nav, to, children, ...props}: ButtonProps) {
    const navigate = useNavigate();

    if (to !== undefined)
        props.onClick = () => navigate({to: to})

    return (
        <button className={`button${wide ? " wide" : ""}${thin ? " thin" : ""}${nav ? " nav" : ""}`} {...props}>
            {children}
        </button>
    )
}

export default Button;
