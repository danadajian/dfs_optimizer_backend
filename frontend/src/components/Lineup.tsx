import React from 'react';
import Table from 'react-bootstrap/Table'
import '../css/Lineup.css'
import {handleRemovePlayerFromLineup} from "../handlers/handleRemovePlayerFromLineup/handleRemovePlayerFromLineup";
import {sumAttribute} from "../helpers/sumAttribute/sumAttribute";
import {LineupPlayer} from "./LineupPlayer";
import {lineupAttributes, State} from "../interfaces";

export const Lineup = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {site, lineup, whiteList, salaryCap} = props.state;

    const pointSum = sumAttribute(lineup, 'projection');
    const salarySum = sumAttribute(lineup, 'salary');

    return (
        <div className="Lineup">
            <h2 className="Dfs-header">Lineup</h2>
            <Table size={"sm"} className="Dfs-grid">
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
                        <LineupPlayer
                            key={playerIndex}
                            player={player}
                            onRemove={() => handleRemovePlayerFromLineup(playerIndex, props.state, props.setState)}
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
            </Table>
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