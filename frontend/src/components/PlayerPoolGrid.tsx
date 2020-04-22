import React, {useState} from 'react'
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
    const [sortSign, setSortSign] = useState(1);

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
        <div className={"Players"}>
            <table style={{borderCollapse: "collapse"}} className={'Dfs-grid'}>
                <PlayerPoolGridHeader sortAttribute={sortAttribute} setSortAttribute={setSortAttribute}
                                      sortSign={sortSign} setSortSign={setSortSign}/>
                <tbody>
                {playerPool.sort((a: playerPoolAttributes, b: playerPoolAttributes) => {
                    return (sortAttribute === 'pricePerPoint') ?
                        sortSign * (b.salary / b.projection - a.salary / a.projection) :
                        // @ts-ignore
                        props.sortSign * (b[props.sortAttribute] - a[props.sortAttribute])
                }).map((player, index) => {
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
                        } else
                            return null;
                    }
                )}
                </tbody>
            </table>
        </div>
    )
};
