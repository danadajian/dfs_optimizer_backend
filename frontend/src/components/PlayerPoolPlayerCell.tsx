import React from "react";
import Table from 'react-bootstrap/Table'
import '../css/PlayerCell.css'
import {playerPoolAttributes} from "../interfaces";

export const PlayerPoolPlayerCell = (props: {
    player: playerPoolAttributes
}) => {
    const {name, status, team, position} = props.player;

    return (
        <td className="Player-cell">
            <Table>
                <tbody>
                <tr className="Player-cell-row">
                    <td>{name} <b>{status}</b></td>
                </tr>
                <tr>
                    <td>{team} {position}</td>
                </tr>
                </tbody>
            </Table>
        </td>
    )
}