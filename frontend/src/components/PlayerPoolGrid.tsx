import React, {useState} from 'react'
import '../css/PlayerPoolGrid.css'
import {playerPoolAttributes, State} from "../interfaces";
import {PlayerPoolPlayer} from "./PlayerPoolPlayer";
import {handleAddPlayerToLineup} from "../handlers/handleAddPlayerToLineup/handleAddPlayerToLineup";
import {handleAddPlayerToBlackList} from "../handlers/handleAddPlayerToBlackList/handleAddPlayerToBlackList";
import {PlayerPoolGridHeader} from "./PlayerPoolGridHeader";

export const PlayerPoolGrid: any = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {playerPool, searchText, filteredPool} = props.state;

    const [sortAttribute, setSortAttribute] = useState('salary');
    const [isAscendingSort, setSortSign] = useState(false);

    return (
        <div className="Player-pool-grid">
            <table className="Dfs-grid">
                <PlayerPoolGridHeader sortAttribute={sortAttribute} setSortAttribute={setSortAttribute}
                                      isAscendingSort={isAscendingSort} setSortSign={setSortSign}/>
                <tbody>
                {playerPool.sort((a: playerPoolAttributes, b: playerPoolAttributes) => {
                    const sortMultiplier = isAscendingSort ? 1 : -1;
                    if (sortAttribute === 'pricePerPoint') {
                        return sortMultiplier * (a.salary / a.projection - b.salary / b.projection)
                    } else {
                        return sortMultiplier * (a[sortAttribute] - b[sortAttribute])
                    }
                }).filter((player: playerPoolAttributes) => {
                    return filteredPool.includes(player) || (!searchText && filteredPool.length === 0)
                }).map((player: playerPoolAttributes, index: number) =>
                    <PlayerPoolPlayer key={index}
                                      player={player}
                                      onPlusClick={() => handleAddPlayerToLineup(index, props.state, props.setState)}
                                      onMinusClick={() => handleAddPlayerToBlackList(index, props.state, props.setState)}
                                      state={props.state}
                                      setState={props.setState}/>
                )
                }
                </tbody>
            </table>
        </div>
    )
};
