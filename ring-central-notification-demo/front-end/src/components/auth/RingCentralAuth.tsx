import React from "react";
import AuthPopup from "../util/AuthPopup";
import CryptoES from "crypto-es";

import {
	UserInfo,
	whoAmICall,
	connectToWebHook,
	InboundCallNotification,
	createSubscription,
} from "../../service/dialer-service";

import NotificationPopup, { NotificationProps } from "./NotificationPopup";

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
	const [notification, setNotification] = React.useState<null | NotificationProps>(null);

	const onCode = React.useCallback(
		(callbackCode: string) => setAuthCode(callbackCode),
		[setAuthCode]
	);
	const onClose = React.useCallback(
		() => setTogglePopup(false),
		[setTogglePopup]
	);
	const onIncomingCall = React.useCallback(
		(event: InboundCallNotification) => {
			setNotification({ leadNumber: event.leadPhoneNumber, leadName: event.lead ? `${event.lead.name} ${event.lead.lastName}` : undefined });
		},[]);

	const toggleButton = (
		<button onClick={() => setTogglePopup(true)}>Authorize</button>
	);

	const showUserInfo = (
		<div>
			{userInfo != null && (
				<>
					<h2>These are the credentials</h2>
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
		if (userInfo != null) {
			connectToWebHook(userInfo.email, onIncomingCall);
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
			{notification != null && <NotificationPopup {...notification}/>}
			{!isAuthorized && !togglePopup && toggleButton}
			{!isAuthorized && togglePopup && (
				<AuthPopup onClose={onClose} onCode={onCode} url={url} />
			)}
			{showUserInfo}
		</>
	);
};

export default RingCentralAuth;
