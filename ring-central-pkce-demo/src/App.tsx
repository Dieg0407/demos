import React from 'react';

import Landing from './Landing';
import EmptyPage from './EmptyPage';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Dashboard from './components/Dashboard';
import { dev, qa } from './env/env';

function App() {
	return (
		<Router>
			<Routes>
				<Route path="/" element={<Landing />} />
				<Route path="/callback" element={<EmptyPage />} />
				<Route path="/dev" element={<Dashboard {...dev}/>} />
				<Route path="/qa" element={<Dashboard {...qa} />} />
			</Routes>
		</Router>
	);
}

export default App;
