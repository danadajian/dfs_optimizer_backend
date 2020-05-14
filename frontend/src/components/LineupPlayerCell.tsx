import * as React from "react";
import '../css/PlayerCell.css'
import {lineupAttributes} from "../interfaces";

export const LineupPlayerCell = (props: {
    player: lineupAttributes
}) => {
    const {name, status, team, opponent, opponentRank} = props.player;
    return (
        <span className="Player-cell">
            <table>
                <tbody>
                <tr className="Player-cell-row">
                    <td>
                        {name && name + ' '}
                        <b>{status}</b>
                    </td>
                </tr>
                <tr className="Player-team-row">
                    <td>
                        {team && <b>{team + ' '}</b>}
                        <span
                            style={getOpponentRankStyle(opponentRank!)}>
                            {opponent}
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </span>
    )
};

export const getOpponentRankStyle = (opponentRank: number) => ({
    color: opponentRank < 9 ? 'red' : opponentRank > 22 ? 'green' : 'black'
})