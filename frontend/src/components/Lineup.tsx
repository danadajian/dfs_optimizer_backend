import React from 'react';
import '../css/Lineup.css'
import {removePlayerFromLineup} from "../helpers/removePlayerFromLineup/removePlayerFromLineup";
import {sumAttribute} from "../helpers/sumAttribute/sumAttribute";
import {LineupPlayer} from "./LineupPlayer";
import {lineupAttributes, State} from "../interfaces";

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
        <div className="Lineup">
            <h2 className="Dfs-header">Lineup</h2>
            <table className="Dfs-grid">
                <tbody>
                <tr style={getLineupStyle(site)}>
                    <th>{}</th>
                    <th>Position</th>
                    <th>Player</th>
                    <th>Projection</th>
                    <th>Salary</th>
                </tr>
                {lineup.map(
                    (player: lineupAttributes, playerIndex: number) =>
                        <LineupPlayer player={player}
                                      onRemove={() => handleRemovePlayer(playerIndex)}
                                      whiteList={whiteList}
                                      site={site}
                        />
                )}
                <tr className="Lineup-total-row">
                    <td>{null}</td>
                    <td>{null}</td>
                    <td>Total</td>
                    <td>{pointSum.toFixed(1)}</td>
                    <td style={getSalaryStyle(salarySum, salaryCap)}>
                        {getFormattedSalary(salarySum)}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    )
};

const getLineupStyle = (site: string) => ({
    backgroundColor: (site === 'Fanduel') ? 'dodgerblue' : 'black'
})

const getSalaryStyle = (salarySum: number, salaryCap: number) => ({
    color: (salarySum > salaryCap) ? 'indianred' : 'black'
})

export const getFormattedSalary = (salary: number) => {
    return '$'.concat(salary.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","))
}