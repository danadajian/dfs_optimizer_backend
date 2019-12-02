import * as React from 'react';

interface playerAttributes {
    playerId: number,
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
    </tr>;

export const BlackList = (props: {
    blackList: number[],
    playerPool: playerAttributes[]
    }) =>
        <table style={{ borderCollapse: 'collapse'}} className={'Draft-grid'}>
            <tbody>
            <tr style={{backgroundColor: 'indianred'}}>
                <th>Player</th>
            </tr>
            {props.blackList.map(
                (playerId) => {
                    return (
                        <Player player={props.playerPool.filter(player => player.playerId === playerId)[0]}/>
                    )
                }
            )}
            </tbody>
        </table>;
