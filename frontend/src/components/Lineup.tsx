import React from 'react';
import {removePlayerFromLineup} from "../helpers/removePlayerFromLineup/removePlayerFromLineup";
import {sumAttribute} from "../helpers/sumAttribute/sumAttribute";
import {LineupPlayer} from "./LineupPlayer";
import {State} from "../interfaces";

export const Lineup = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {site, lineup, whiteList, salaryCap, lineupPositions, displayMatrix} = props.state;

    const handleRemovePlayer = (playerIndex: number) => {
        const {newLineup, newWhiteList}: any = removePlayerFromLineup(playerIndex, lineup, whiteList, lineupPositions, displayMatrix);
        props.setState({
            ...props.state,
            lineup: newLineup,
            whiteList: newWhiteList,
            searchText: '',
            filteredPool: []
        });
    };

    const pointSum = sumAttribute(lineup, 'projection');
    const salarySum = sumAttribute(lineup, 'salary');

    return (
        <div className={"Lineup"}>
            <h2 className={"Dfs-header"}>Lineup</h2>
            <table style={{borderCollapse: "collapse"}} className={'Dfs-grid'}>
                <tbody>
                <tr style={{backgroundColor: (site === 'Fanduel') ? 'dodgerblue' : 'black'}}>
                    <th>{}</th>
                    <th>Position</th>
                    <th>Player</th>
                    <th>Projection</th>
                    <th>Salary</th>
                </tr>
                {lineup.map(
                    (player, playerIndex) => (
                        <LineupPlayer player={player}
                                onRemove={() => handleRemovePlayer(playerIndex)}
                                whiteList={whiteList}
                                site={site}
                        />
                    )
                )}
                <tr style={{fontWeight: 'bold'}}>
                    <td>{null}</td>
                    <td>{null}</td>
                    <td>Total</td>
                    <td>{pointSum.toFixed(1)}</td>
                    <td style={{color: (salarySum > salaryCap) ? 'indianred' : 'black'}}>
                        {'$'.concat(salarySum.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","))}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    )
};
