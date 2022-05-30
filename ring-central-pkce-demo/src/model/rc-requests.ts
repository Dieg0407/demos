export interface GetAccessToken {
  authCode: string;
  clientId: string;
  codeVerifier: string;
  authUrl: string; 
  callbackUrl: string;
}

export interface AccessTokenResponse {
  access_token: string;
  token_type: string;
  expires_in: number;
  refresh_token: string;
  refresh_token_expires_in: number;
  scope: string;
  owner_id: string;
  endpoint_id: string;
}

interface UserContact {
  firstName: string;
  lastName: string;
  email: string;
  businessPhone: string;
}

export interface UserProfileResponse {
  id: number;
  contact: UserContact;
}

export interface UserPhone {
  phoneNumber: string;
  usageType: string;
}