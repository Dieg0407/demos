export const dialerServiceBaseUrl = 'http://localhost:8081/api';

export interface UserInfo {
  name: string;
  email: string;
}

export const whoAmICall = async (authToken: string): Promise<UserInfo> => {
  const myHeaders = new Headers();
		myHeaders.append("Content-Type", "application/json");
		myHeaders.append("x-rc-auth-token", authToken);

		const response = await fetch(`${dialerServiceBaseUrl}/v1/user-info`, {
			method: 'get',
			headers: myHeaders,
		});

    return response.json();
}

export const createSubscription = async (authToken: string) => {
  const myHeaders = new Headers();
		myHeaders.append("Content-Type", "application/json");
		myHeaders.append("x-rc-auth-token", authToken);

		const response = await fetch(`${dialerServiceBaseUrl}/v1/subscription`, {
			method: 'post',
			headers: myHeaders,
		});

    return response.json();
} 