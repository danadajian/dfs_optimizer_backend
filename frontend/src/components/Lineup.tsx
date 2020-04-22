import * as React from 'react';
import {lineupAttributes} from "../interfaces";

interface playerProps {
    player: lineupAttributes,
    onRemove: () => void,
    whiteList: number[],
    site: string
}

const Player = (props: playerProps) => {
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
                        style={{'color': opponentRank && opponentRank < 9 ? 'red' :
                                opponentRank && opponentRank > 22 ? 'green' :
                                    'black'}}>
                        {opponent}
                    </text>
                </tr>
            </td>
            <td style={{fontWeight: (position) ? 'normal' : 'bold'}}>{roundedProjection}</td>
            <td style={{fontWeight: (position) ? 'normal' : 'bold'}}>{formattedSalary}</td>
        </tr>
    );
};

export const Lineup = (props: {
    dfsLineup: lineupAttributes[],
    removePlayerFunction: (playerIndex: number) => void,
    site: string,
    whiteList: number[],
    pointSum: number,
    salarySum: number,
    salaryCap: number
}) =>
    <table style={{borderCollapse: "collapse"}} className={'Dfs-grid'}>
        <tbody>
        <tr style={{backgroundColor: (props.site === 'Fanduel') ? 'dodgerblue' : 'black'}}>
            <th>{}</th>
            <th>Position</th>
            <th>Player</th>
            <th>Projection</th>
            <th>Salary</th>
        </tr>
        {props.dfsLineup.map(
            (player, playerIndex) => (
                <Player player={player}
                        onRemove={() => props.removePlayerFunction(playerIndex)}
                        whiteList={props.whiteList}
                        site={props.site}
                />
            )
        )}
        <tr style={{fontWeight: 'bold'}}>
            <td>{null}</td>
            <td>{null}</td>
            <td>Total</td>
            <td>{props.pointSum.toFixed(1)}</td>
            <td style={{color: (props.salarySum > props.salaryCap) ? 'indianred' : 'black'}}>
                {'$'.concat(props.salarySum.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","))}
            </td>
        </tr>
        </tbody>
    </table>;
