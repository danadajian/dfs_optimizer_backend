import * as React from 'react';

interface playerAttributes {
    playerId: number,
    position: string,
    displayPosition: string,
    team: string,
    name: string,
    status: string,
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
        if (isCaptain && props.site === 'DraftKings') {
            let multiplier = (parseFloat(props.player.displayPosition.split('(')[1].substring(0, 3)));
            salary *= multiplier;
        }
        formattedSalary = '$'.concat(salary.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","))
    }

    return (
        <tr style={{
            backgroundColor: (
                props.player.name && props.whiteList.includes(props.player.playerId)) ? 'lightgreen' : 'white'
        }}>
            <td>
                {props.player.position && props.player.name &&
                <button onClick={props.onRemove} style={{fontWeight: 'bold'}}>X</button>}
            </td>
            <td>{props.player.displayPosition}</td>
            <td>
                <tr>
                    <b>{props.player.name + ' '}</b>
                    <b style={{color: 'red'}}>{props.player.status}</b>
                </tr>
                <tr>
                    <b style={{color: 'blue'}}>{props.player.team + ' '}</b>
                    <text style={{
                        'color': props.player.opponentRank < 9 ?
                            'red' : props.player.opponentRank > 22 ? 'green' : 'black'
                    }}>{props.player.opponent}</text>
                </tr>
            </td>
            <td style={{fontWeight: (props.player.position) ? 'normal' : 'bold'}}>{roundedProjection}</td>
            <td style={{fontWeight: (props.player.position) ? 'normal' : 'bold'}}>{formattedSalary}</td>
        </tr>
    );
};

export const Lineup = (props: {
    dfsLineup: playerAttributes[],
    removePlayerFunction: (playerIndex: number) => void,
    site: string,
    whiteList: number[],
    pointSum: number,
    salarySum: number,
    cap: number
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
            <td style={{color: (props.salarySum > props.cap) ? 'indianred' : 'black'}}>
                {'$'.concat(props.salarySum.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","))}
            </td>
        </tr>
        </tbody>
    </table>;
