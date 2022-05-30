import React from "react";
import { RingCentralEnv } from "../env/env";
import { AccessTokenResponse } from "../model/rc-requests";
import { getAccessToken } from "../service/rc-service";
import { generate, VerifyVaribles } from "../service/verifier";
import AuthPopup from "./auth/AuthPopup";
import Operations from "./Operations";

const parsed = new URL(window.location.href);
const callbackUrl = `${parsed.protocol}//${parsed.hostname}/callback`;
const url = (
	rcProps: RingCentralEnv,
	verifyVariables: VerifyVaribles
): string => {
	return `${rcProps.rcUrl}/restapi/oauth/authorize?response_type=code&redirect_uri=${callbackUrl}&client_id=${rcProps.clientId}&code_challenge=${verifyVariables.challenge}&code_challenge_method=${verifyVariables.method}`;
};

const Dashboard = (props: RingCentralEnv) => {
	const [verifier, setVerifier] = React.useState<VerifyVaribles>(generate());
	const [isConnected, setIsConnected] = React.useState<boolean>(false);
	const [togglePopup, setTogglePopup] = React.useState<boolean>(false);
	const [authCode, setAuthCode] = React.useState<string>("");
	const [credentials, setCredentials] = React.useState<AccessTokenResponse | undefined>();

	const disconnect = async () => {
		setIsConnected(false);
		setVerifier(generate());
		setCredentials(undefined);
		setAuthCode("");
	};
	const onCode = React.useCallback(
		(callbackCode: string) => setAuthCode(callbackCode),
		[setAuthCode]
	);
	const onClose = React.useCallback(
		() => setTogglePopup(false),
		[setTogglePopup]
	);

	React.useEffect(() => {
		const exchange = async () => {
			const res = await getAccessToken({
				authCode: authCode,
				authUrl: `${props.rcUrl}/restapi/oauth/token`,
				clientId: props.clientId,
				codeVerifier: verifier.verifier,
				callbackUrl: callbackUrl
			});

			console.log(res);
			setCredentials(res);
			setIsConnected(true);
		};

		if (authCode !== "") exchange();
	}, [authCode]);

	return (
		<>
			<div style={{ width: "98%", textAlign: "center" }}>
				<h1>{props.name}</h1>
			</div>
			{!isConnected && (
				<div style={{ width: "98%" }}>
					<button onClick={() => setTogglePopup(true)}>Connect to RC</button>
				</div>
			)}
			{togglePopup && (
				<AuthPopup
					onClose={onClose}
					onCode={onCode}
					url={url(props, verifier)}
				/>
			)}
			{isConnected && (
				<div style={{ width: "98%" }}>
					<button onClick={disconnect}>Disconnect from RC</button>
				</div>
			)}
			{isConnected && credentials !== undefined && (
				<div style={{ width: "98%" }}>
					<Operations accessCode={credentials?.access_token ?? ""} rcUrl={props.rcUrl} telephonyUrl={props.telephonyUrl}/>
				</div>
			)}
		</>
	);
};

export default Dashboard;
