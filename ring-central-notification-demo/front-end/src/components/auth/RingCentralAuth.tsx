import React from "react";
import AuthPopup from "../util/AuthPopup";
import CryptoES from "crypto-es";
import { Client } from "@stomp/stompjs";

import {
	UserInfo,
	whoAmICall,
	connectToWebHook,
	InboundCallNotification,
	createSubscription,
} from "../../service/dialer-service";

const genRandom64 = () => {
	const bytes: number[] = [];
	for (let i = 0; i < 32; i = i + 1) {
		bytes.push(Math.floor(Math.random() * 256));
	}
	return window
		.btoa(String.fromCharCode.apply(null, bytes))
		.replace(/\+/g, "-")
		.replace(/\//g, "_")
		.replace(/=/g, "");
};
const codeVerifier = genRandom64();
const codeChallenge = CryptoES.SHA256(codeVerifier)
	.toString(CryptoES.enc.Base64)
	.replace(/\+/g, "-")
	.replace(/\//g, "_")
	.replace(/=/g, "");

const clientId = "HY6uDzgiTwe-Omm4uzUs3g";
const parsed = new URL(window.location.href);
const callbackUrl = `${parsed.protocol}//${parsed.hostname}/callback`;
const url = `https://platform.devtest.ringcentral.com/restapi/oauth/authorize?response_type=code&redirect_uri=${callbackUrl}&client_id=${clientId}&code_challenge=${codeChallenge}&code_challenge_method=${"S256"}`;
const authUrl = "https://platform.devtest.ringcentral.com/restapi/oauth/token";

const RingCentralAuth: React.FC<any> = () => {
	const [authCode, setAuthCode] = React.useState<string>("");
	const [isAuthorized, setIsAuthorized] = React.useState<boolean>(false);
	const [togglePopup, setTogglePopup] = React.useState<boolean>(false);
	const [credentials, setCredentials] = React.useState<null | any>(null);
	const [userInfo, setUserInfo] = React.useState<null | UserInfo>(null);

	const onCode = React.useCallback(
		(callbackCode: string) => setAuthCode(callbackCode),
		[setAuthCode]
	);
	const onClose = React.useCallback(
		() => setTogglePopup(false),
		[setTogglePopup]
	);
	const onIncommingCall = React.useCallback(
		(event: InboundCallNotification) => console.log(event),
		[]
	);

	const toggleButton = (
		<button onClick={() => setTogglePopup(true)}>Authorize</button>
	);

	const showUserInfo = (
		<div>
			{userInfo != null && (
				<>
					<h2>This are the credentials</h2>
					<div>
						<label>Name: </label>
						<p>{userInfo?.name}</p>
					</div>
					<div>
						<label>Email: </label>
						<p>{userInfo?.email}</p>
					</div>
				</>
			)}
			{userInfo == null && <h2>User not logged in</h2>}
		</div>
	);
	React.useEffect(() => {
		const base64data = window.btoa("dieg0407@hotmail.com");
		const client = new Client({
			brokerURL: "ws://bca0-38-25-17-223.ngrok.io/incoming-calls",
			reconnectDelay: 5000,
			heartbeatIncoming: 4000,
			heartbeatOutgoing: 4000,
			logRawCommunication: true,
			connectHeaders: {},
			debug: (x) => console.log(x),
		});

		client.onConnect = (frame) => {
			console.log("client connected");
			client.subscribe("/topic/answered-calls/" + base64data, (message) => {
				const notification: InboundCallNotification = JSON.parse(message.body);
				console.log(notification);

				message.ack();
			});
		};

		client.activate();
	}, []);

	React.useEffect(() => {
		if (userInfo != null) {
			connectToWebHook(userInfo.email, onIncommingCall);
		}
	}, [userInfo]);

	React.useEffect(() => {
		const callWhoAmI = async () => {
			await createSubscription(credentials?.access_token);
			const user = await whoAmICall(credentials?.access_token);
			setUserInfo(user);
		};
		if (credentials != null) callWhoAmI();
	}, [credentials]);

	React.useEffect(() => {
		const exchange = async () => {
			const formData = new URLSearchParams();
			formData.append("code", authCode);
			formData.append("grant_type", "authorization_code");
			formData.append("client_id", clientId);
			formData.append("code_verifier", codeVerifier);
			formData.append("redirect_uri", callbackUrl);

			const myHeaders = new Headers();
			myHeaders.append("Content-Type", "application/x-www-form-urlencoded");
			myHeaders.append("Accept", "application/json");
			try {
				const requestOptions = {
					method: "POST",
					headers: myHeaders,
					body: formData,
				};

				const response = await fetch(authUrl, requestOptions);
				const json = await response.json();

				setCredentials(json);
				setIsAuthorized(true);
			} catch (e: any) {
				console.error(e);
			}
		};

		if (authCode !== "") exchange();
	}, [authCode]);

	return (
		<>
			{!isAuthorized && !togglePopup && toggleButton}
			{!isAuthorized && togglePopup && (
				<AuthPopup onClose={onClose} onCode={onCode} url={url} />
			)}
			{showUserInfo}
		</>
	);
};

export default RingCentralAuth;
