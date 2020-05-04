import React from 'react';
import '../css/PlayerPool.css'
import {State} from "../interfaces";
import {SearchBar} from "./SearchBar";
import {FilterBar} from "./FilterBar";
import {PlayerPoolGrid} from "./PlayerPoolGrid";

export const PlayerPool = (props: {
    state: State,
    setState: (state: State) => void,
}) => {
    return (
        <div className="Player-pool">
            <h2 className="Dfs-header">Players</h2>
            <SearchBar state={props.state} setState={props.setState}/>
            <FilterBar state={props.state} setState={props.setState}/>
            <PlayerPoolGrid state={props.state} setState={props.setState}/>
        </div>
    )
};
