import React from "react";
import { UserPhone, UserProfileResponse } from "../model/rc-requests";
import { getUserPhones, getUserProfile } from "../service/rc-service";
import { createCall, sendSMS } from "../service/telephony-service";

export interface OperationProps {
	rcUrl: string;
  telephonyUrl: string;
	accessCode: string;
}

const Operations: React.FC<OperationProps> = (props) => {
	const [profile, setProfile] = React.useState<UserProfileResponse>();
	const [leadPhoneNumber, setLeadPhoneNumber] = React.useState<string>("");
	const [message, setMessage] = React.useState<string>("");
	const [phones, setPhones] = React.useState<UserPhone[]>([]);

	React.useEffect(() => {
		const exchange = async () => {
			const res = await getUserProfile(props.rcUrl, props.accessCode);
			const res2 = await getUserPhones(props.rcUrl, props.accessCode);

			console.log(res);
			setProfile(res);
			setPhones(res2);
		};

		exchange();
	}, []);

  const call = async () => {
    try {
      await createCall(props.telephonyUrl, props.accessCode, leadPhoneNumber);
      alert('call created!');
    }
    catch(e) {
      alert('Failed to create a call');
    }
  }
  const sms = async () => {
    try {
      await sendSMS(props.telephonyUrl, props.accessCode, leadPhoneNumber, message);
      alert('message sent!');
    }
    catch(e) {
      alert('Failed to send the message');
    }
  }
	return (
		<>
			<div style={{ width: "100%" }}>
				<h2>User Profile</h2>
				<h3>Name: {profile?.contact.firstName}</h3>
				<h3>LastName: {profile?.contact.lastName}</h3>
				<h3>Email: {profile?.contact.email}</h3>

				{phones.length > 0 && (
					phones.map(phone => <h4>Phone Number: {phone.phoneNumber} | Type: {phone.usageType}</h4>)
				)}
			</div>
			<hr style={{ borderTop: "3px solid #bbb" }} />
			<div style={{ width: "100%" }}>
				<h2>Lead Info</h2>
				<div style={{width: '100%'}}>
					<label>
						Phone Number:{" "}
						<input
							type="text"
							value={leadPhoneNumber}
							onChange={(event) => setLeadPhoneNumber(event.target.value)}
						/>
					</label>

					<label>
						Message:{" "}
						<input
							type="text"
							value={message}
							onChange={(event) => setMessage(event.target.value)}
						/>
					</label>
				</div>

				<div style={{width:'100%'}}>
					<button onClick={sms}>Send SMS</button>
					<button onClick={call}>Call</button>
				</div>
			</div>
		</>
	);
};

export default Operations;
