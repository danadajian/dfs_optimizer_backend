import {lineupPlayerProps} from "../interfaces";
import * as React from "react";

export const LineupPlayer = (props: lineupPlayerProps) => {
    const {playerId, name, status, team, position, projection, salary, displayPosition, opponentRank, opponent} = props.player;
    let roundedProjection;
    let formattedSalary;
    if (salary) {
        let finalSalary = salary;
        roundedProjection = projection && parseFloat(projection.toFixed(1));
        let isCaptain = displayPosition.includes('x Points)');
        if (isCaptain && props.site === 'DraftKings') {
            let multiplier = (parseFloat(displayPosition.split('(')[1].substring(0, 3)));
            finalSalary *= multiplier;
        }
        formattedSalary = '$'.concat(finalSalary.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","))
    }

    return (
        <tr style={{backgroundColor: name && props.whiteList.includes(playerId) ? 'lightgreen' : 'white'}}>
            <td>
                {position && name &&
                <button onClick={props.onRemove} style={{fontWeight: 'bold'}}>X</button>}
            </td>
            <td>{displayPosition}</td>
            <td>
                <tr>
                    <b>{name + ' '}</b>
                    <b style={{color: 'red'}}>{status}</b>
                </tr>
                <tr>
                    <b style={{color: 'blue'}}>{team + ' '}</b>
                    <text
                        style={{
                            'color': opponentRank && opponentRank < 9 ? 'red' :
                                opponentRank && opponentRank > 22 ? 'green' :
                                    'black'
                        }}>
                        {opponent}
                    </text>
                </tr>
            </td>
            <td style={{fontWeight: (position) ? 'normal' : 'bold'}}>{roundedProjection}</td>
            <td style={{fontWeight: (position) ? 'normal' : 'bold'}}>{formattedSalary}</td>
        </tr>
    );
};
