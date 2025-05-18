import * as React from "react";
import './Container.scss'

function Container(
	{ gap, align, children, ...props }:
	{ gap?: "sm" | "md" | "lg" | "xl" | undefined, align?: 'center' | 'left' | 'right', children: React.ReactNode, props?: never }
) {
	return (
		<div className={`container ${gap ?? ""} ${align ?? ""}`} {...props}>
			{children}
		</div>
	)
}

export default Container
