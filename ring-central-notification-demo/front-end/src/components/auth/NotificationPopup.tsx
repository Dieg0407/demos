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
				top: 0,
				right: 0,
				width: 300,
				height: 50,
        borderRadius: '1px',
        borderWidth: '1px',
        borderColor: 'black'
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
