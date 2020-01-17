import * as React from 'react';
import {getOrdinalString} from "../functions/getOrdinalString";

const plus = require("../icons/plus.ico") as any;
const minus = require("../icons/minus.ico") as any;
const up = require("../icons/up.svg") as any;
const down = require("../icons/down.svg") as any;

interface playerAttributes {
    playerId: number,
    position: string,
    displayPosition: string,
    team: string,
    name: string,
    status: string,
    projection: number,
    salary: number,
    opponent: string,
    opponentRank: number,
    gameDate: string,
    spread: string,
    overUnder: number
}

interface playerProps {
    player: playerAttributes,
    onPlusClick: () => void,
    onMinusClick: () => void,
    whiteList: number[],
    blackList: number[],
    salarySum: number,
    cap: number
}

const Player = (props: playerProps) =>
    <tr style={{backgroundColor: (props.whiteList.includes(props.player.playerId)) ? 'lightgreen' :
            (props.blackList.includes(props.player.playerId)) ? 'indianred' : 'white'}}>
        <td>
            <tr style={{fontWeight: 'bold'}}>
                {props.player.name} <b style={{color: 'red'}}>{props.player.status}</b>
            </tr>
            <tr>{props.player.team} {props.player.position}</tr>
        </td>
        <td>{props.player.projection.toFixed(1)}</td>
        <td style={{color: (props.salarySum + props.player.salary > props.cap) ? 'red' : 'black'}}>
            ${props.player.salary.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}
        </td>
        <td>
            ${(props.player.salary / props.player.projection).toFixed(0)
            .toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}
        </td>
        <td>
            {props.player.opponent + ' '}
            <b style={{'color': props.player.opponentRank < 9 ?
                    'red' : props.player.opponentRank > 22 ? 'green' : 'black'}}>
                {getOrdinalString(props.player.opponentRank)}
            </b>
        </td>
        <td>{props.player.spread}</td>
        <td>{props.player.overUnder}</td>
        <td>{props.player.gameDate}</td>
        <td>
            <img src={plus} alt={"add"} onClick={props.onPlusClick} style={{height: '3vmin'}}/>
        </td>
        <td>
            <img src={minus} alt={"remove"} onClick={props.onMinusClick} style={{height: '3vmin'}}/>
        </td>
    </tr>;

export const PlayerPool = (props: {
    playerList: playerAttributes[],
    filterList: playerAttributes[],
    whiteListFunction: (index: number) => void,
    blackListFunction: (index: number) => void,
    toggleSort: (attribute: string) => void,
    sortAttribute: string,
    sortSign: number,
    whiteList: number[],
    blackList: number[],
    salarySum: number,
    cap: number}) =>
        <table style={{ borderCollapse: 'collapse'}} className={'Draft-grid'}>
            <tbody>
            <tr style={{backgroundColor: 'lightgray'}}>
                <th>Player</th>
                <th>Projection
                        <img src={props.sortSign === 1 ? down : up} alt={"sort"}
                             onClick={() => props.toggleSort('projection')}
                             style={{marginLeft: '1vmin', height: '2vmin',
                                 backgroundColor: props.sortAttribute === 'projection' ? 'red' : 'white'}}/>
                </th>
                <th>Salary
                    <img src={props.sortSign === 1 ? down : up} alt={"sort"}
                         onClick={() => props.toggleSort('salary')}
                         style={{marginLeft: '1vmin', height: '2vmin',
                             backgroundColor: props.sortAttribute === 'salary' ? 'red' : 'white'}}/>
                </th>
                <th>Price Per Point
                    <img src={props.sortSign === 1 ? down : up} alt={"sort"}
                         onClick={() => props.toggleSort('pricePerPoint')}
                         style={{marginLeft: '1vmin', height: '2vmin',
                             backgroundColor: props.sortAttribute === 'pricePerPoint' ? 'red' : 'white'}}/>
                </th>
                <th>Opponent</th>
                <th>Spread</th>
                <th>O/U</th>
                <th>Game Date</th>
                <th>Add</th>
                <th>Blacklist</th>
            </tr>
            {props.playerList.sort((a, b) => {
                return (props.sortAttribute === 'pricePerPoint') ?
                    props.sortSign * (b.salary / b.projection - a.salary / a.projection) :
                    // @ts-ignore
                    props.sortSign*(b[props.sortAttribute] - a[props.sortAttribute])
            }).map(
                (player, index) => {
                    if (!props.filterList || props.filterList.includes(player)) {
                        return (
                            <Player key={index}
                                    player={player}
                                    onPlusClick={() => props.whiteListFunction(index)}
                                    onMinusClick={() => props.blackListFunction(index)}
                                    whiteList={props.whiteList}
                                    blackList={props.blackList}
                                    salarySum={props.salarySum}
                                    cap={props.cap}
                            />
                        )
                    } else return null;
                }
            )}
            </tbody>
        </table>;