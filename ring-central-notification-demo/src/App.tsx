import React from "react";

import Landing from "./Landing";
import EmptyPage from "./Empty";

import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

function App() {
	return (
		<Router>
			<Routes>
				<Route path="/" element={<Landing />} />
				<Route path="/callback" element={<EmptyPage />} />
			</Routes>
		</Router>
	);
}

export default App;
