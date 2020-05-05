import React from 'react';
import InputGroup from 'react-bootstrap/InputGroup'
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
            <InputGroup>
                <SearchBar state={props.state} setState={props.setState}/>
                <InputGroup.Append>
                    <FilterBar state={props.state} setState={props.setState}/>
                </InputGroup.Append>
            </InputGroup>
            <PlayerPoolGrid state={props.state} setState={props.setState}/>
        </div>
    )
};
