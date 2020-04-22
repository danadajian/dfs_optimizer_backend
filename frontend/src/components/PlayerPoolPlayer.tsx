import React from "react";
import {playerPoolAttributes, State} from "../interfaces";
import {getOrdinalString} from "../helpers/getOrdinalString/getOrdinalString";
import {sumAttribute} from "../helpers/sumAttribute/sumAttribute";

const plusIcon = require("../icons/plus.ico") as any;
const minusIcon = require("../icons/minus.ico") as any;

export const PlayerPoolPlayer = (props: {
    player: playerPoolAttributes,
    onPlusClick: () => void,
    onMinusClick: () => void,
    state: State,
    setState: (state: State) => void,
}) => {
    const {lineup, whiteList, blackList, salaryCap} = props.state;
    const {playerId, name, status, team, position, projection, salary, opponentRank, opponent, spread, overUnder, gameDate} = props.player;

    const salarySum = sumAttribute(lineup, 'salary');

    return (
        <tr style={{
            backgroundColor: (whiteList.includes(playerId)) ? 'lightgreen' :
                (blackList.includes(playerId)) ? 'indianred' : 'white'
        }}>
            <td>
                <img src={plusIcon} alt={"add"} onClick={props.onPlusClick} style={{height: '3vmin'}}/>
            </td>
            <td>
                <img src={minusIcon} alt={"remove"} onClick={props.onMinusClick} style={{height: '3vmin'}}/>
            </td>
            <td>
                <tr style={{fontWeight: 'bold'}}>
                    {name} <b style={{color: 'red'}}>{status}</b>
                </tr>
                <tr>{team} {position}</tr>
            </td>
            <td>{projection.toFixed(1)}</td>
            <td style={{color: (salarySum + salary > salaryCap) ? 'red' : 'black'}}>
                ${salary.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}
            </td>
            <td>
                ${(salary / projection)
                .toFixed(0)
                .toString()
                .replace(/\B(?=(\d{3})+(?!\d))/g, ",")}
            </td>
            <td>
                {opponent + ' '}
                <b style={{
                    'color': opponentRank < 9 ?
                        'red' : opponentRank > 22 ? 'green' : 'black'
                }}>
                    {getOrdinalString(opponentRank)}
                </b>
            </td>
            <td>{spread}</td>
            <td>{overUnder}</td>
            <td>{gameDate}</td>
        </tr>
    )
};
