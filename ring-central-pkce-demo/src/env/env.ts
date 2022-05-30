
export interface RingCentralEnv {
  name: string;
  clientId: string;
  telephonyUrl: string;
  rcUrl: string;
}

export const dev: RingCentralEnv = {
  name: "Dev App",
  clientId: "8hjV2S9JRWm9ymOYrdy9Ag",
  telephonyUrl: "https://approveengine-dev.autoapprove.com/telephony-service/api/v1",
  rcUrl: "https://platform.devtest.ringcentral.com"
}

export const qa: RingCentralEnv = {
  name: "Qa App",
  clientId: "o5h1G1G_SlCnN4tROuMFEg",
  telephonyUrl: "https://approveengine-qa.autoapprove.com/telephony-service/api/v1",
  rcUrl: "https://platform.devtest.ringcentral.com"
}