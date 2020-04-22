import * as React from 'react';
import {lineupAttributes} from "../interfaces";

interface playerProps {
    player: lineupAttributes
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
    playerPool: lineupAttributes[]
}) => {
    return (
        <div className={"Blacklist"}>
            <h2 className={"Dfs-header"}>Blacklist</h2>
            <table style={{borderCollapse: 'collapse'}} className={'Dfs-grid'}>
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
            </table>
        </div>
    )
};
