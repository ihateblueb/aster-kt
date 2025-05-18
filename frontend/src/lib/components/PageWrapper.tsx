import * as React from "react";
import './PageWrapper.scss'

function PageWrapper({ padding = "full", center = false, children }: { padding: "full" | "timeline" | "none", center: boolean, children: React.ReactNode }) {
	return <div className={`pageWrapper ${padding} ${center ? ' center' : ''}`}>
		{children}
	</div>
}

export default PageWrapper
