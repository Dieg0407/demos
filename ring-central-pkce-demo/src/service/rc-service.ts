import { AccessTokenResponse, GetAccessToken, UserPhone, UserProfileResponse } from "../model/rc-requests";

export const getAccessToken = async ({
	authCode,
	clientId,
	codeVerifier,
	authUrl,
	callbackUrl,
}: GetAccessToken): Promise<AccessTokenResponse> => {
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

		return {
			...json,
		};
	} catch (e: any) {
		throw e;
	}
};

export const getUserProfile = async (baseUrl: string, accessToken: string): Promise<UserProfileResponse> => {
	try {
		const myHeaders = new Headers();
		myHeaders.append("Authorization", `Bearer ${accessToken}`);

		const requestOptions = {
			method: "GET",
			headers: myHeaders
		};

		const response = await fetch(`${baseUrl}/restapi/v1.0/account/~/extension/~`, requestOptions);
		const json = await response.json();

		return {
			...json,
		};
	} catch (e: any) {
		throw e;
	}
};

export const getUserPhones = async (baseUrl: string, accessToken: string): Promise<UserPhone[]> => {
	try {
		const myHeaders = new Headers();
		myHeaders.append("Authorization", `Bearer ${accessToken}`);

		const requestOptions = {
			method: "GET",
			headers: myHeaders
		};

		const response = await fetch(`${baseUrl}/restapi/v1.0/account/~/extension/~/phone-number`, requestOptions);
		const json = await response.json();
		const phones: UserPhone[] = json.records;

		return phones.filter(i => i.usageType === 'DirectNumber' || i.usageType === 'MainCompanyNumber');
	} catch (e: any) {
		throw e;
	}
};
