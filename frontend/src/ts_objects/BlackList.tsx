import * as React from 'react';

interface playerAttributes {
    position: string,
    team: string,
    name: string,
    salary: number
}

interface playerProps {
    player: playerAttributes
}

const Player = (props: playerProps) =>
    <tr>
        <td>
            <tr style={{fontWeight: 'bold'}}>{props.player.name}</tr>
            <tr>{props.player.team} {props.player.position}</tr>
        </td>
        <td>
            {'$'.concat(props.player.salary.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","))}
        </td>
    </tr>;

export const BlackList = (props: {
    blackList: playerAttributes[]
    }) =>
        <table style={{ borderCollapse: 'collapse'}} className={'Draft-grid'}>
            <tbody>
            <tr style={{backgroundColor: 'indianred'}}>
                <th>Player</th>
                <th>Salary</th>
            </tr>
            {props.blackList.sort((a, b) => b.salary - a.salary).map(
                (player) => {
                    return (
                        <Player player={player}/>
                    )
                }
            )}
            </tbody>
        </table>;
