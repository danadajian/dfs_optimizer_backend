import * as React from 'react';
import {getOrdinalString} from "../resources/getOrdinalString/getOrdinalString";
import {useState} from "react";
import {playerPoolAttributes} from "../LineupAttributes";

const plus = require("../icons/plus.ico") as any;
const minus = require("../icons/minus.ico") as any;
const up = require("../icons/up.svg") as any;
const down = require("../icons/down.svg") as any;

interface playerProps {
    player: playerPoolAttributes,
    onPlusClick: () => void,
    onMinusClick: () => void,
    whiteList: number[],
    blackList: number[],
    salarySum: number,
    salaryCap: number
}

const Player = (props: playerProps) => {
    const {playerId, name, status, team, position, projection, salary, opponentRank, opponent, spread, overUnder, gameDate} = props.player;
    return (
        <tr style={{
            backgroundColor: (props.whiteList.includes(playerId)) ? 'lightgreen' :
                (props.blackList.includes(playerId)) ? 'indianred' : 'white'
        }}>
            <td>
                <img src={plus} alt={"add"} onClick={props.onPlusClick} style={{height: '3vmin'}}/>
            </td>
            <td>
                <img src={minus} alt={"remove"} onClick={props.onMinusClick} style={{height: '3vmin'}}/>
            </td>
            <td>
                <tr style={{fontWeight: 'bold'}}>
                    {name} <b style={{color: 'red'}}>{status}</b>
                </tr>
                <tr>{team} {position}</tr>
            </td>
            <td>{projection.toFixed(1)}</td>
            <td style={{color: (props.salarySum + salary > props.salaryCap) ? 'red' : 'black'}}>
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

export const PlayerPool = (props: {
    playerList: playerPoolAttributes[],
    filterList: playerPoolAttributes[],
    addPlayerFunction: (index: number) => void,
    blackListFunction: (index: number) => void,
    whiteList: number[],
    blackList: number[],
    salarySum: number,
    salaryCap: number
}) => {

    const [sortAttribute, setSortAttribute] = useState('salary');
    const [sortSign, setSortSign] = useState(1);

    const sortBy = (attribute: string) => {
        if (attribute === sortAttribute)
            setSortSign(-sortSign);
        else
            setSortAttribute(attribute);
    };

    return (
        <table style={{borderCollapse: "collapse"}} className={'Dfs-grid'}>
            <thead>
            <tr className={"Dfs-grid-header"}>
                <th>{}</th>
                <th>{}</th>
                <th>Player</th>
                <th>Projection
                    <img src={sortSign === 1 ? down : up} alt={"sort"}
                         onClick={() => sortBy('projection')}
                         style={{
                             marginLeft: '1vmin', height: '2vmin',
                             backgroundColor: sortAttribute === 'projection' ? 'red' : 'white'
                         }}/>
                </th>
                <th>Salary
                    <img src={sortSign === 1 ? down : up} alt={"sort"}
                         onClick={() => sortBy('salary')}
                         style={{
                             marginLeft: '1vmin', height: '2vmin',
                             backgroundColor: sortAttribute === 'salary' ? 'red' : 'white'
                         }}/>
                </th>
                <th>$/Point
                    <img src={sortSign === 1 ? down : up} alt={"sort"}
                         onClick={() => sortBy('pricePerPoint')}
                         style={{
                             marginLeft: '1vmin', height: '2vmin',
                             backgroundColor: sortAttribute === 'pricePerPoint' ? 'red' : 'white'
                         }}/>
                </th>
                <th>Opponent</th>
                <th>Spread</th>
                <th>O/U</th>
                <th>Game Date</th>
            </tr>
            </thead>
            <tbody>
            {props.playerList.sort((a: playerPoolAttributes, b: playerPoolAttributes) => {
                return (sortAttribute === 'pricePerPoint') ?
                    sortSign * (b.salary / b.projection - a.salary / a.projection) :
                    // @ts-ignore
                    props.sortSign * (b[props.sortAttribute] - a[props.sortAttribute])
            }).map(
                (player, index) => {
                    if (props.filterList.includes(player)) {
                        return (
                            <Player key={index}
                                    player={player}
                                    onPlusClick={() => props.addPlayerFunction(index)}
                                    onMinusClick={() => props.blackListFunction(index)}
                                    whiteList={props.whiteList}
                                    blackList={props.blackList}
                                    salarySum={props.salarySum}
                                    salaryCap={props.salaryCap}
                            />
                        )
                    } else return null;
                }
            )}
            </tbody>
        </table>
    )
};