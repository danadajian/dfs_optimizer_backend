import React, {useState} from 'react'
import '../css/PlayerPoolGrid.css'
import {playerPoolAttributes, State} from "../interfaces";
import {PlayerPoolPlayer} from "./PlayerPoolPlayer";
import {addPlayerToLineup} from "../helpers/addPlayerToLineup/addPlayerToLineup";
import {addPlayerToBlackList} from "../helpers/addPlayerToBlackList/addPlayerToBlackList";
import {PlayerPoolGridHeader} from "./PlayerPoolGridHeader";

export const PlayerPoolGrid: any = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {playerPool, filteredPool, lineup, whiteList, blackList, lineupPositions, displayMatrix} = props.state;

    const [sortAttribute, setSortAttribute] = useState('salary');
    const [isAscendingSort, setSortSign] = useState(false);

    const handleAddPlayer = (playerIndex: number) => {
        const {newLineup, newWhiteList, newBlackList}: any = addPlayerToLineup(playerIndex, playerPool, lineup, whiteList, blackList);
        props.setState({
            ...props.state,
            lineup: newLineup,
            whiteList: newWhiteList,
            blackList: newBlackList,
            searchText: '',
            filteredPool: []
        });
    };

    const handleToggleBlackList = (playerIndex: number) => {
        const {newLineup, newWhiteList, newBlackList}: any = addPlayerToBlackList(playerIndex, playerPool, lineup,
            whiteList, blackList, lineupPositions, displayMatrix);
        props.setState({
            ...props.state,
            lineup: newLineup,
            whiteList: newWhiteList,
            blackList: newBlackList,
            searchText: '',
            filteredPool: []
        });
    };

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
                }).map((player: playerPoolAttributes, index: number) => {
                        if (filteredPool.includes(player)) {
                            return (
                                <PlayerPoolPlayer key={index}
                                                  player={player}
                                                  onPlusClick={() => handleAddPlayer(index)}
                                                  onMinusClick={() => handleToggleBlackList(index)}
                                                  state={props.state}
                                                  setState={props.setState}
                                />
                            )
                        }
                    }
                )}
                </tbody>
            </table>
        </div>
    )
};
