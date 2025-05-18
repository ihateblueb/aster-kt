import * as React from "react";
import './Input.scss';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
	id?: string;
	label?: string;
	type?: string;
	wide?: boolean;
	placeholder: string;
	value: string;
}

function Input(
	{ id, label, type, wide, placeholder, value, ...props }:
	InputProps
) {
	return (
		<>
			<div>
				{label ? (
					<label className={`inputLabel ${wide ? "wide" : ""}`} id={id}>{label}</label>
				) : null}
				<input className={`input ${wide ? "wide" : ""}`} id={id} type={type} placeholder={placeholder} value={value} {...props} />
			</div>
		</>
	)
}

export default Input
