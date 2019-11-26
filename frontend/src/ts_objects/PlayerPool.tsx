import * as React from 'react';

interface playerAttributes {
    playerId: number,
    position: string,
    team: string,
    name: string,
    projection: number,
    salary: number,
    opponent: string
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

const plus = require("../resources/plus.ico") as any;
const minus = require("../resources/minus.ico") as any;

const Player = (props: playerProps) =>
    <tr style={{backgroundColor: (props.whiteList.includes(props.player.playerId)) ? 'lightgreen' :
            (props.blackList.includes(props.player.playerId)) ? 'indianred' : 'white'}}>
        <td>
            <tr style={{fontWeight: 'bold'}}>{props.player.name}</tr>
            <tr>{props.player.team} {props.player.position}</tr>
        </td>
        <td>{props.player.opponent}</td>
        <td style={{color: (props.salarySum + props.player.salary > props.cap) ? 'red' : 'black'}}>
            {'$'.concat(props.player.salary.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","))}
        </td>
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
    whiteList: number[],
    blackList: number[],
    salarySum: number,
    cap: number}) =>
        <table style={{ borderCollapse: 'collapse'}} className={'Draft-grid'}>
            <tbody>
            <tr style={{backgroundColor: 'lightgray'}}>
                <th>Player</th>
                <th>Opponent</th>
                <th>Salary</th>
                <th>Add</th>
                <th>Blacklist</th>
            </tr>
            {props.playerList.sort((a, b) => b.salary - a.salary).map(
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
