import * as React from "react";
import './Container.scss'

function Container(
    {gap, align, clazz, children, ...props}:
    {
        gap?: "sm" | "md" | "lg" | "xl" | undefined,
        align?: 'center' | 'left' | 'right' | 'horizontal',
        clazz?: string,
        children: React.ReactNode,
        props?: never
    }
) {
    return (
        <div
            className={`container${gap ? " gap-" + gap : ""}${align ? " " + align : ""}${clazz ? " " + clazz : ""}`} {...props}>
            {children}
        </div>
    )
}

export default Container
