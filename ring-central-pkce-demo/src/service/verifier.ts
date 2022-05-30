import CryptoES from "crypto-es";

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

export interface VerifyVaribles {
  verifier: string;
  challenge: string;
  method: string;
}

export const generate = (): VerifyVaribles => {
	const codeVerifier = genRandom64();
	const codeChallenge = CryptoES.SHA256(codeVerifier)
		.toString(CryptoES.enc.Base64)
		.replace(/\+/g, "-")
		.replace(/\//g, "_")
		.replace(/=/g, "");

	return {
		verifier: codeVerifier,
    challenge: codeChallenge,
    method: "S256"
	};
};
