import * as React from 'react';
import {getOrdinalString} from "../functions/getOrdinalString";

interface playerAttributes {
    playerId: number,
    position: string,
    displayPosition: string,
    team: string,
    name: string,
    projection: number,
    salary: number,
    opponent: string,
    opponentRank: number,
    gameDate: string
}

interface playerProps {
    player: playerAttributes,
    onRemove: () => void,
    whiteList: number[],
    site: string
}

const Player = (props: playerProps) => {
    let roundedProjection;
    let formattedSalary;
    if (props.player.salary) {
        roundedProjection = parseFloat(props.player.projection.toFixed(1));
        let salary = props.player.salary;
        let isCaptain = props.player.displayPosition.includes('x Points)');
        if (isCaptain && props.site === 'dk') {
            let multiplier = (parseFloat(props.player.displayPosition.split('(')[1].substring(0, 3)));
            salary *= multiplier;
        }
        formattedSalary = '$'.concat(salary.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","))
    }

    return (
        <tr style={{backgroundColor: (
            props.player.name && props.whiteList.includes(props.player.playerId)) ? 'lightgreen' : 'white'}}>
            <td>{props.player.displayPosition}</td>
            <td>{props.player.team}</td>
            <td style={{fontWeight: (props.player.position) ? 'normal' : 'bold'}}>{props.player.name}</td>
            <td style={{fontWeight: (props.player.position) ? 'normal' : 'bold'}}>{roundedProjection}</td>
            <td style={{fontWeight: (props.player.position) ? 'normal' : 'bold'}}>{formattedSalary}</td>
            <td>{props.player.opponent}</td>
            <td style={{fontWeight: 'bold',
                'color': props.player.opponentRank < 9 ? 'red' : props.player.opponentRank > 22 ? 'green' : 'black'}}>
                {getOrdinalString(props.player.opponentRank)}</td>
            <td>{props.player.gameDate}</td>
            <td>
                {props.player.position && props.player.name && <button onClick={props.onRemove} style={{fontWeight: 'bold'}}>X</button>}
            </td>
        </tr>
    );
};

export const Lineup = (props: {
    dfsLineup: playerAttributes[],
    removePlayer: (playerIndex: number) => void,
    site: string,
    whiteList: number[],
    pointSum: number,
    salarySum: number,
    cap: number}) =>
        <table className={'Dfs-grid'}>
            <tbody>
            <tr style={{backgroundColor: (props.site === 'fd') ? 'dodgerblue' : 'black'}}>
                <th>Position</th>
                <th>Team</th>
                <th>Player</th>
                <th>Projection</th>
                <th>Salary</th>
                <th>Opponent</th>
                <th>Opp Rank</th>
                <th>Game Date</th>
                <th>Remove</th>
            </tr>
            {props.dfsLineup.map(
                (player, playerIndex) => (
                    <Player player={player}
                            onRemove={() => props.removePlayer(playerIndex)}
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
                <td style={{color: (props.salarySum > props.cap) ? 'indianred' : 'black'}}>
                    {'$'.concat(props.salarySum.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","))}
                </td>
                <td>{null}</td>
                <td>{null}</td>
                <td>{null}</td>
            </tr>
            </tbody>
        </table>;
