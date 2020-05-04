import * as React from "react";
import '../css/LineupPlayer.css'
import {lineupPlayerProps} from "../interfaces";
import {getFormattedSalary} from "./Lineup";

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
        formattedSalary = getFormattedSalary(finalSalary)
    }

    return (
        <tr style={getPlayerRowStyle(props.whiteList, playerId)}>
            <td>
                {position && name &&
                <button className="Remove-button" onClick={props.onRemove}>X</button>}
            </td>
            <td>{displayPosition}</td>
            <td>
                <tr>
                    {name && <b>{name + ' '}</b>}
                    <b style={{color: 'red'}}>{status}</b>
                </tr>
                <tr>
                    {team && <b style={{color: 'blue'}}>{team + ' '}</b>}
                    <text
                        style={getOpponentRankStyle(opponentRank!)}>
                        {opponent}
                    </text>
                </tr>
            </td>
            <td style={{fontWeight: position ? 'normal' : 'bold'}}>{roundedProjection}</td>
            <td style={{fontWeight: position ? 'normal' : 'bold'}}>{formattedSalary}</td>
        </tr>
    );
};

const getPlayerRowStyle = (whiteList: number[], playerId: number) => ({
    backgroundColor: whiteList.includes(playerId) ? 'lightgreen' : 'white'
})

export const getOpponentRankStyle = (opponentRank: number) => ({
    color: opponentRank < 9 ? 'red' : opponentRank > 22 ? 'green' : 'black'
})
