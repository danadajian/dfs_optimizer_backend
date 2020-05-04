import React from "react";
import '../css/FilterBar.css'
import {playerPoolAttributes, State} from "../interfaces";
import {getSetFromArray} from "../helpers/getSetFromArray/getSetFromArray";
import {handleFilterPlayers} from "../handlers/handleFilterPlayers/handleFilterPlayers";

export const FilterBar: any = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {playerPool} = props.state;

    return (
        <div className="Filter-bar">
            <button onClick={() =>
                handleFilterPlayers('position', 'All', props.state, props.setState)}>All
            </button>
            {
                getSetFromArray(playerPool.map((player: playerPoolAttributes) => player.position))
                    .map((position, index: number) =>
                        <button
                            key={index}
                            onClick={() =>
                                handleFilterPlayers('position', position, props.state, props.setState)}>{position}</button>
                    )
            }
            <select onChange={(event: any) =>
                handleFilterPlayers('team', event.target.value, props.state, props.setState)}>
                <option defaultValue={'All'}>All</option>
                {getSetFromArray(playerPool.map((player: playerPoolAttributes) => player.team))
                    .sort()
                    .map((team, index: number) =>
                        <option key={index} value={team}>{team}</option>
                    )}
            </select>
        </div>
    )
};