import * as React from "react";
import './PageHeader.scss'

function PageHeader({ icon, title, children }: { icon: React.ReactNode, title: string, children: React.ReactNode }) {
	return <div className={"pageHeader"}>
		<div className={"icon"}>
			{icon}
		</div>
		<div className={"title"}>
			{title}
		</div>
		<div className={"children"}>
			{children}
		</div>
	</div>
}

export default PageHeader
