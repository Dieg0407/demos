import React from "react";
import ReactDOM from "react-dom";

export type NotificationProps = {
	leadNumber: string;
	leadName?: string;
};

const Modal = (props: NotificationProps) => {
	return (
		<div
			style={{
				position: "absolute",
				right: 10,
				borderStyle: "solid",
				borderColor: 'black',
				padding: 20,
				display: "flex",
				flexDirection: "column",
				alignContent: "center",
				backgroundColor: 'rgba(81, 50, 162, 0.05)',
				textDecoration: "  text-decoration: none solid rgb(24, 26, 29)"
			}}
		>
			<div style={{ position: "relative", width: "100%" }}>
				<h3>You're in a call with the number {props.leadNumber}</h3>
			</div>
			<div style={{ position: "relative", width: "100%" }}>
        { props.leadName && <p>Caller name: {props.leadName}</p> }
        { !props.leadName && <p>Unknown caller</p>}
			</div>
		</div>
	);
};

const NotificationPopup = (props: NotificationProps) => {
	return (
		<>
			{ReactDOM.createPortal(
				<Modal {...props} />,
				document.getElementById("notifications-modal")!
			)}
		</>
	);
};

export default NotificationPopup;
