export const sendSMS = async (
	baseUrl: string,
	accessToken: string,
	leadPhone: string,
	message: string
) => {
	try {
		const myHeaders = new Headers();
		myHeaders.append("x-rc-auth-token", accessToken);
		myHeaders.append("Content-type", `application/json`);
		myHeaders.append("Accept", `application/json`);

		const requestOptions = {
			method: "POST",
			headers: myHeaders,
			body: JSON.stringify({
				to: leadPhone,
				message: message,
			}),
		};

		const response = await fetch(`${baseUrl}/sms`, requestOptions);

		if (!response.ok) {
			throw new Error("failed to send sms");
		}

		const json = await response.json();

		return {
			...json,
		};
	} catch (e: any) {
		throw e;
	}
};

export const createCall = async (
	baseUrl: string,
	accessToken: string,
	leadPhone: string
) => {
	try {
		const myHeaders = new Headers();
		myHeaders.append("x-rc-auth-token", accessToken);
		myHeaders.append("Content-type", `application/json`);
		myHeaders.append("Accept", `application/json`);

		const requestOptions = {
			method: "POST",
			headers: myHeaders,
			body: JSON.stringify({
				to: leadPhone,
			}),
		};

		const response = await fetch(`${baseUrl}/call`, requestOptions);

		if (!response.ok) {
			throw new Error("failed to create call");
		}
		const json = await response.json();

		return {
			...json,
		};
	} catch (e: any) {
		throw e;
	}
};
