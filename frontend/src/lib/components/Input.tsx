import * as React from "react";
import './Input.scss';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
	label?: string;
	wide?: boolean;
}

function Input(
	{ label, wide, ...props }:
	InputProps
) {
	return (
		<div>
			{label ? (
				<label className={`inputLabel ${wide ? "wide" : ""}`}>{label}</label>
			) : null}
			<input className={`input ${wide ? "wide" : ""}`} {...props} />
		</div>
	)
}

export default Input
