import './Button.scss'
import * as React from "react";

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
	wide?: boolean;
	thin?: boolean;
	children: React.ReactNode;
}

function Button({ wide, thin, children, ...props }: ButtonProps) {
	return (
		<button className={`button ${wide ? "wide" : ""} ${thin ? "thin" : ""}`} {...props}>
			{children}
		</button>
	)
}

export default Button;
