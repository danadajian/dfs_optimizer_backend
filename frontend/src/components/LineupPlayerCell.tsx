import * as React from "react";
import '../css/PlayerCell.css'
import {lineupPlayerProps} from "../interfaces";
import {getOpponentRankStyle} from "./LineupPlayer";

export const LineupPlayerCell = (props: lineupPlayerProps) => {
    const {name, status, team, opponent, opponentRank} = props.player;
    return (
        <td className="Player-cell">
            <table>
                <tbody>
                <tr>
                    <td>
                        {name && <b>{name + ' '}</b>}
                        <b style={{color: 'red'}}>{status}</b>
                    </td>
                </tr>
                <tr>
                    <td>
                        {team && <b style={{color: 'blue'}}>{team + ' '}</b>}
                        <span
                            style={getOpponentRankStyle(opponentRank!)}>
                            {opponent}
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    )
};