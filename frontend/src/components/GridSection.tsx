import {Loading} from "./Loading";
import {Optimizing} from "./Optimizing";
import {BlackList} from "./BlackList";
import {PlayerPool} from "./PlayerPool";
import {sumAttribute} from "../helpers/sumAttribute/sumAttribute";
import {getSetFromArray} from "../helpers/getSetFromArray/getSetFromArray";
import {Lineup} from "./Lineup";
import React, {useState} from "react";
import {filterPlayers} from "../helpers/filterPlayers/filterPlayers";
import {addPlayerToLineup} from "../helpers/addPlayerToLineup/addPlayerToLineup";
import {removePlayerFromLineup} from "../helpers/removePlayerFromLineup/removePlayerFromLineup";
import {addPlayerToBlackList} from "../helpers/addPlayerToBlackList/addPlayerToBlackList";
import {State} from "../interfaces";

const search = require("../icons/search.ico") as any;

export const GridSection: any = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {
        isLoading, isOptimizing, site, sport, contest, playerPool, lineup, whiteList, blackList, lineupPositions,
        displayMatrix, loadingText, salaryCap
    } = props.state;

    const [searchText, setSearchText] = useState('');
    const [filteredPool, setFilteredPool] = useState(props.state.filteredPool);

    const handleFilter = (attribute: string, value: string) => {
        const {searchText, filteredPool} = filterPlayers(attribute, value, playerPool);
        setSearchText(searchText);
        setFilteredPool(filteredPool);
    };

    const handleAddPlayer = (playerIndex: number) => {
        const {newLineup, newWhiteList, newBlackList}: any = addPlayerToLineup(playerIndex, playerPool, lineup, whiteList, blackList);
        props.setState({
            ...props.state,
            lineup: newLineup,
            whiteList: newWhiteList,
            blackList: newBlackList,
        });
        setSearchText('');
        setFilteredPool([]);
    };

    const handleRemovePlayer = (playerIndex: number) => {
        const {newLineup, newWhiteList}: any = removePlayerFromLineup(playerIndex, lineup, whiteList, lineupPositions, displayMatrix);
        props.setState({
            ...props.state,
            lineup: newLineup,
            whiteList: newWhiteList
        });
        setSearchText('');
        setFilteredPool([]);
    };

    const handleToggleBlackList = (playerIndex: number) => {
        const {newLineup, newWhiteList, newBlackList}: any = addPlayerToBlackList(playerIndex, playerPool, lineup,
            whiteList, blackList, lineupPositions, displayMatrix);
        props.setState({
            ...props.state,
            lineup: newLineup,
            whiteList: newWhiteList,
            blackList: newBlackList,
        });
        setSearchText('');
        setFilteredPool([]);
    };

    return (
        isLoading ? <Loading sport={sport} loadingText={loadingText}/> :
            isOptimizing ? <Optimizing sport={sport}/> :
                site && sport && contest &&
                <div className={"Dfs-grid-section"}>
                    <div className={"Lineup"}>
                        <h2 className={"Dfs-header"}>Lineup</h2>
                        <Lineup dfsLineup={lineup} removePlayerFunction={handleRemovePlayer} site={site}
                                whiteList={whiteList} pointSum={sumAttribute(lineup, 'projection')}
                                salarySum={sumAttribute(lineup, 'salary')} salaryCap={salaryCap}/>
                    </div>
                    <div className={"Player-pool"}>
                        <h2 className={"Dfs-header"}>Players</h2>
                        <div style={{display: 'flex', flexDirection: 'column'}}>
                            {!filteredPool &&
                            <img src={search} style={{height: '3vmin', position: 'absolute'}} alt="search"/>}
                            <input type="text" style={{height: '25px', width: '90%'}}
                                   value={searchText}
                                   onChange={(event) =>
                                       handleFilter('name', event.target.value)}>{null}</input>
                        </div>
                        <div style={{display: 'flex'}}>
                            <button onClick={() => handleFilter('position', 'All')}>All</button>
                            {
                                getSetFromArray(playerPool.map((player) => player.position))
                                    .map((position) =>
                                        <button
                                            onClick={() =>
                                                handleFilter('position', position)}>{position}</button>
                                    )
                            }
                            <select onChange={(event) => handleFilter('team', event.target.value)}>
                                <option defaultValue={'All'}>All</option>
                                {getSetFromArray(playerPool.map((player) => player.team))
                                    .sort()
                                    .map((team) =>
                                        <option value={team}>{team}</option>
                                    )}
                            </select>
                        </div>
                        <div className={"Players"}>
                            <PlayerPool playerList={playerPool} filterList={filteredPool}
                                        addPlayerFunction={handleAddPlayer} blackListFunction={handleToggleBlackList}
                                        whiteList={whiteList} blackList={blackList}
                                        salarySum={sumAttribute(lineup, 'salary')} salaryCap={salaryCap}/>
                        </div>
                    </div>
                    <div className={"Blacklist"}>
                        <h2 className={"Dfs-header"}>Blacklist</h2>
                        <BlackList blackList={blackList} playerPool={playerPool}/>
                    </div>
                </div>
    );
};