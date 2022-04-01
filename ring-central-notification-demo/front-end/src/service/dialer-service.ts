import { Client } from "@stomp/stompjs";

const parsed = new URL(window.location.href);
export const dialerServiceBaseUrl = `${parsed.protocol}//${parsed.hostname}/dialer/api`;
export const dialerIncomingCallWS = `wss://${parsed.hostname}/incoming-calls`;

export interface UserInfo {
	name: string;
	email: string;
}

export interface Lead {
	id: string;
	name: string;
	lastName: string;
}

export interface InboundCallNotification {
	userEmail: string;
	leadPhoneNumber: string;
	lead?: Lead;
}

export const whoAmICall = async (authToken: string): Promise<UserInfo> => {
	const myHeaders = new Headers();
	myHeaders.append("Content-Type", "application/json");
	myHeaders.append("x-rc-auth-token", authToken);

	const response = await fetch(`${dialerServiceBaseUrl}/v1/user-info`, {
		method: "get",
		headers: myHeaders,
	});

	return response.json();
};

export const createSubscription = async (authToken: string) => {
	const myHeaders = new Headers();
	myHeaders.append("Content-Type", "application/json");
	myHeaders.append("x-rc-auth-token", authToken);

	const response = await fetch(`${dialerServiceBaseUrl}/v1/subscription`, {
		method: "post",
		headers: myHeaders,
	});

	return response.status;
};

export const connectToWebHook = (
	email: string,
	onMessageArrived: (event: InboundCallNotification) => void
) => {
	
	const base64data = window.btoa(email);
	const client = new Client({
		brokerURL: dialerIncomingCallWS,
		//webSocketFactory: () => new SockJS.default("http://localhost:8081/api/incoming-calls", {}),
		reconnectDelay: 5000,
		heartbeatIncoming: 4000,
		heartbeatOutgoing: 4000,
		logRawCommunication: true,
		connectHeaders: {
			login: '',
			passcode: ''
		},
		debug: (x) => console.log(x),
	});

	client.onConnect = (frame) => {
		console.log("client connected");
		client.subscribe("/topic/answered-calls/" + base64data, (message) => {
			const notification: InboundCallNotification = JSON.parse(message.body);
			onMessageArrived(notification);

			message.ack();
		});
	};

	client.activate();
	return client;
};
