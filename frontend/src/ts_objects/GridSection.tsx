import {Loading} from "./Loading";
import {Optimizing} from "./Optimizing";
import {BlackList} from "./BlackList";
import {PlayerPool} from "./PlayerPool";
import {sumAttribute} from "../functions/sumAttribute";
import {arrayToSet} from "../functions/arrayToSet";
import {Lineup} from "./Lineup";
import React from "react";

const search = require("../icons/search.ico") as any;

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

export const GridSection = (props: {
    isLoading: boolean,
    isOptimizing: boolean,
    loadingText: string,
    site: string,
    sport: string,
    contest: string,
    lineup: playerAttributes[],
    playerPool: playerAttributes[],
    filteredPool: playerAttributes[],
    whiteList: number[],
    blackList: number[],
    searchText: string,
    filterPlayers: (attribute: string, value: string) => void,
    addToLineup: (playerIndex: number) => void,
    removeFromLineup: (playerIndex: number) => void,
    toggleBlackList: (playerIndex: number) => void,
    sortAttribute: string,
    sortSign: number,
    toggleSort: (attribute: string) => void,
    salaryCap: number
}) =>
    props.isLoading ? <Loading sport={props.sport} loadingText={props.loadingText}/> :
        props.isOptimizing ? <Optimizing sport={props.sport}/> :
    props.site && props.sport && props.contest &&
    <div className={"Dfs-grid-section"}>
        <div className={"Player-list-box"}>
            <h2 className={"Dfs-header"}>Blacklist</h2>
            <BlackList blackList={props.blackList} playerPool={props.playerPool}/>
        </div>
        <div>
            <h2 className={"Dfs-header"}>Players</h2>
            <div style={{display: 'flex', flexDirection: 'column'}}>
                {!props.filteredPool && <img src={search} style={{height: '3vmin', position: 'absolute'}} alt="search"/>}
                <input type="text" style={{height: '25px', width: '90%'}}
                       value={props.searchText}
                       onChange={(event) => props.filterPlayers('name', event.target.value)}>{null}</input>
            </div>
            <div style={{display: 'flex'}}>
                <button onClick={() => props.filterPlayers('position', 'All')}>All</button>
                {
                    arrayToSet(props.playerPool.map((player) => player.position))
                        .map((position) =>
                            <button onClick={() => props.filterPlayers('position', position)}>{position}</button>
                        )
                }
                <select onChange={(event) => props.filterPlayers('team', event.target.value)}>
                    <option defaultValue={'All'}>All</option>
                    {arrayToSet(props.playerPool.map((player) => player.team))
                        .sort()
                        .map((team) =>
                            <option value={team}>{team}</option>
                    )}
                </select>
            </div>
            <div className={"Player-list-box"}>
                <PlayerPool playerList={props.playerPool} filterList={props.filteredPool}
                            whiteListFunction={props.addToLineup} blackListFunction={props.toggleBlackList}
                            toggleSort={props.toggleSort} sortAttribute={props.sortAttribute} sortSign={props.sortSign}
                            whiteList={props.whiteList} blackList={props.blackList}
                            salarySum={sumAttribute(props.lineup, 'salary')} cap={props.salaryCap}/>
            </div>
        </div>
        <div>
            <h2 className={"Dfs-header"}>Lineup</h2>
            <Lineup dfsLineup={props.lineup} removePlayer={props.removeFromLineup} site={props.site}
                    whiteList={props.whiteList} pointSum={sumAttribute(props.lineup, 'projection')}
                    salarySum={sumAttribute(props.lineup, 'salary')} cap={props.salaryCap}/>
        </div>
    </div>;