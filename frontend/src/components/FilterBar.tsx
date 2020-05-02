import React from "react";
import '../css/FilterBar.css'
import {playerPoolAttributes} from "../interfaces";
import {getSetFromArray} from "../helpers/getSetFromArray/getSetFromArray";

export const FilterBar: any = (props: {
    playerPool: playerPoolAttributes[]
    handleFilter: (attribute: string, value: string) => void
}) => {
    return (
        <div className="Filter-bar">
            <button onClick={() => props.handleFilter('position', 'All')}>All</button>
            {
                getSetFromArray(props.playerPool.map((player) => player.position))
                    .map((position) =>
                        <button
                            onClick={() =>
                                props.handleFilter('position', position)}>{position}</button>
                    )
            }
            <select onChange={(event) => props.handleFilter('team', event.target.value)}>
                <option defaultValue={'All'}>All</option>
                {getSetFromArray(props.playerPool.map((player) => player.team))
                    .sort()
                    .map((team) =>
                        <option value={team}>{team}</option>
                    )}
            </select>
        </div>
    )
};