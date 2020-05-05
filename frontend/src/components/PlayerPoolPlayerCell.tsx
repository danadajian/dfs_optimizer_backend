import React from "react";
import '../css/PlayerCell.css'
import {playerPoolAttributes} from "../interfaces";

export const PlayerPoolPlayerCell = (props: {
    player: playerPoolAttributes
}) => {
    const {name, status, team, position} = props.player;

    return (
        <td className="Player-cell">
            <table>
                <tbody>
                <tr style={{fontWeight: 'bold'}}>
                    <td>{name} <b style={{color: 'red'}}>{status}</b></td>
                </tr>
                <tr>
                    <td>{team} {position}</td>
                </tr>
                </tbody>
            </table>
        </td>
    )
}