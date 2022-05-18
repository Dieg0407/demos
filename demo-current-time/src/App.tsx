import React, { useEffect } from "react";
import "./App.css";

const format = (timezone: string): string => {
	const formatter = new Intl.DateTimeFormat([], {
		timeZone: timezone,
		hour: "numeric",
		minute: "numeric",
	});

  return formatter.format(new Date(2022, 2, 12, 15, 30, 8));
};

const rows = [
	{ id: 1, name: "lead 01", timezone: "America/Chicago" },
	{ id: 2, name: "lead 02", timezone: "America/Martinique" },
	{ id: 3, name: "lead 03", timezone: "America/Cambridge_Bay" },
	{ id: 4, name: "lead 04", timezone: "America/Hermosillo" },
	{ id: 5, name: "lead 05", timezone: "America/Indiana/Tell_City" },
	{ id: 6, name: "lead 06", timezone: "America/Indiana/Petersburg" },
	{ id: 7, name: "lead 07", timezone: "America/Inuvik" },
	{ id: 8, name: "lead 08", timezone: "America/Knox_IN" },
	{ id: 9, name: "lead 09", timezone: "America/Rosario" },
	{ id: 10, name: "lead 10", timezone: "America/Pangnirtung" },
];

function App() {
	const [info, setInfo] = React.useState<any[]>(rows.map(r => {
    return {
      ...r,
      currentTime: format(r.timezone)
    }
  }));

  useEffect(() => {
    const interval = setInterval(() => {
      console.log('This will run every minute!');
      setInfo(prev => prev.map(r => {
        return {
          ...r,
          currentTime: format(r.timezone)
        }
      }))
    }, 60000);
    return () => clearInterval(interval);
  });

	return (
		<table>
			<thead>
				<tr>
					<th>id</th>
					<th>name</th>
					<th>current time</th>
				</tr>
			</thead>
			<tbody>
				{info.map((i) => (
					<tr>
						<td>{i.id}</td>
						<td>{i.name}</td>
						<td>{i.currentTime}</td>
					</tr>
				))}
			</tbody>
		</table>
	);
}

export default App;
