import React, { useEffect, useState, useCallback } from "react";

type IWindowProps = {
	url: string;
	title: string;
	width: number;
	height: number;
};

type IPopupProps = {
	url: string;
	onClose: () => void;
	onCode: (code: string, params: URLSearchParams) => void;
};

const createPopup = ({ url, title, height, width }: IWindowProps) => {
	const left = window.screenX + (window.outerWidth - width) / 2;
	const top = window.screenY + (window.outerHeight - height) / 2.5;
	return window.open(url, title, `width=${width},height=${height},left=${left},top=${top}`);
};

const AuthPopup: React.FC<IPopupProps> = ({
	url,
	onCode,
	onClose,
}: IPopupProps) => {
  const title = "";
	const width = 500;
	const height = 500;
	const [externalWindow, setExternalWindow] = useState<Window | null>();

	const onContainerClick = useCallback(() => {
		setExternalWindow(
			createPopup({
				url,
				title,
				width,
				height,
			})
		);
	}, [setExternalWindow]);

	useEffect(() => {
		const interval = setInterval(() => {
			if (externalWindow?.location?.href) {
				const currentUrl = externalWindow?.location?.href;
				const params = new URL(currentUrl).searchParams;
				const code = params.get("code");
				if (!code) {
					return;
				}
				onCode(code, params);
				onClose();
				externalWindow.close();
			}
		}, 700);
		return () => clearInterval(interval);
	}, [externalWindow]);

  useEffect(() => onContainerClick(), []);
	return (
		<div />
	);
};

export default AuthPopup;
