import React from "react";
import AuthPopup from "../util/AuthPopup";

const codeVerifier = "EmVNUsZ6SF3YFrf0c7J3mPZE9ENtZbMwPMGZl7NZ2iM";
const codeChallenge = "VNbCb0eR3ODPx1H16iSTwI3NdY8rzcVgVGeO7JZ1F1Y";
const clientId = "HY6uDzgiTwe-Omm4uzUs3g";
const callbackUrl = "https://f46a-38-25-17-223.ngrok.io/callback";
const url = `https://platform.devtest.ringcentral.com/restapi/oauth/authorize?response_type=code&redirect_uri=${callbackUrl}&client_id=${clientId}&code_challenge=${codeChallenge}&code_challenge_method=${"S256"}`;
const authUrl = "https://platform.devtest.ringcentral.com/restapi/oauth/token";

const RingCentralAuth: React.FC<any> = () => {
	const [authCode, setAuthCode] = React.useState<string>("");
	const [isAuthorized, setIsAuthorized] = React.useState<boolean>(false);
	const [togglePopup, setTogglePopup] = React.useState<boolean>(false);
	const [credentials, setCredentials] = React.useState<null | any>(null);
	const [calleeNumber, setCalleeNumber] =
		React.useState<string>("+17202786767");
	const [callerNumber, setCallerNumber] =
		React.useState<string>("+15188315313");

	const onCode = React.useCallback(
		(callbackCode: string) => setAuthCode(callbackCode),
		[setAuthCode]
	);
	const onClose = React.useCallback(
		() => setTogglePopup(false),
		[setTogglePopup]
	);

	const invokeCallOutMethod = async () => {
		console.log(credentials);
		const myHeaders = new Headers();
		myHeaders.append("Content-Type", "application/json");
		myHeaders.append("x-ringcentral-token", credentials?.access_token);

		const response = await fetch("http://localhost/call", {
			method: "post",
			headers: myHeaders,
			body: JSON.stringify({
				to: calleeNumber,
				userPhoneNumber: callerNumber,
			}),
		});

		try {
			console.log(await response.json());
		} catch (e) {
			console.error(e);
		}
	};

	const toggleButton = (
		<button onClick={() => setTogglePopup(true)}>Authorize</button>
	);

	const showCredentials = (
		<div>
			<h2>This are the credentials</h2>
			<div>
				<label>Auth Token</label>
				<p>{credentials?.access_token}</p>
			</div>
			<div>
				<label>Refresh Token</label>
				<p>{credentials?.refresh_token}</p>
			</div>
			{isAuthorized && (
				<div>
					<label>Caller Number</label>
					<input
						value={callerNumber}
						onChange={(e) => setCallerNumber(e.target.value)}
					/>
				</div>
			)}
			{isAuthorized && (
				<div>
					<label>Callee Number</label>
					<input
						value={calleeNumber}
						onChange={(e) => setCalleeNumber(e.target.value)}
					/>
				</div>
			)}
			{isAuthorized && (
				<button onClick={invokeCallOutMethod}> Invoke Call Method</button>
			)}
		</div>
	);

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
			{showCredentials}
		</>
	);
};

export default RingCentralAuth;
