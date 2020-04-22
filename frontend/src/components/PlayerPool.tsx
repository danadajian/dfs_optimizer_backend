import React from 'react';
import {State} from "../interfaces";
import {filterPlayers} from "../helpers/filterPlayers/filterPlayers";
import {SearchBar} from "./SearchBar";
import {FilterBar} from "./FilterBar";
import {PlayerPoolGrid} from "./PlayerPoolGrid";

export const PlayerPool = (props: {
    state: State,
    setState: (state: State) => void,
}) => {
    const {searchText, playerPool, filteredPool} = props.state;

    const handleFilter = (attribute: string, value: string) => {
        const {searchText, newFilteredPool}: any = filterPlayers(attribute, value, filteredPool);
        props.setState({
            ...props.state,
            searchText,
            filteredPool: newFilteredPool
        });
    };

    return (
        <div className={"Player-pool"}>
            <h2 className={"Dfs-header"}>Players</h2>
            <SearchBar searchText={searchText} filteredPool={filteredPool} handleFilter={handleFilter}/>
            <FilterBar playerPool={playerPool} handleFilter={handleFilter}/>
            <PlayerPoolGrid state={props.state} setState={props.setState}/>
        </div>
    )
};
