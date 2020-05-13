import * as React from 'react';
import Table from 'react-bootstrap/Table'
import '../css/BlackList.css'
import {blackListPlayerProps, lineupAttributes} from "../interfaces";

export const BlackList = (props: {
    blackList: number[],
    playerPool: lineupAttributes[]
}) => {
    return (
        <div className="Blacklist">
            <h2>Blacklist</h2>
            <Table size={"sm"}>
                <tbody>
                <tr>
                    <th>Player</th>
                </tr>
                {props.blackList.map(
                    (playerId: number) =>
                        <Player player={props.playerPool.find(player => player.playerId === playerId)!}/>
                )}
                </tbody>
            </Table>
        </div>
    )
};

const Player = (props: blackListPlayerProps) =>
    <tr>
        <td>
            <tr className="Player-name">{props.player.name}</tr>
            <tr>{props.player.team} {props.player.position}</tr>
        </td>
    </tr>;
