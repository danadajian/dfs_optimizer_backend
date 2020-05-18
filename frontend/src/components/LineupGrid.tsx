import React from "react";
import '../css/Lineup.css'
import '../css/LineupPlayer.css'
import {LineupPlayerCell} from "./LineupPlayerCell";
import {getFormattedSalary} from "../helpers/getFormattedSalary/getFormattedSalary";
import {getAdjustedDraftKingsSalary} from "../helpers/getAdjustedDraftKingsSalary/getAdjustedDraftKingsSalary";
import {handleRemovePlayerFromLineup} from "../handlers/handleRemovePlayerFromLineup/handleRemovePlayerFromLineup";
import {sumAttribute} from "../helpers/sumAttribute/sumAttribute";
import {StateProps} from "../interfaces";
import BootstrapTable from "react-bootstrap-table-next";

const removeIcon = require('../icons/remove.svg')

export const LineupGrid = (props: StateProps) => {
    const {site, lineup, whiteList, salaryCap} = props.state;

    const pointSum = sumAttribute(lineup, 'projection');
    const salarySum = sumAttribute(lineup, 'salary');

    const lineupHeaderClass = (site === 'Fanduel') ? "Fanduel-header" : "DraftKings-header";

    const salaryStyle = {
        color: (salarySum > salaryCap) ? 'indianred' : 'black'
    };

    const rowStyle = (row: any) => ({
        backgroundColor: whiteList.includes(row.playerId) ? 'lightgreen' : 'white'
    });

    const columns = [{
        dataField: 'remove',
        text: '',
        footer: '',
        events: {
            onClick: (event: any) => {
                const playerId = Number(event.target.getAttribute('data-player-id'));
                if (playerId > 0) {
                    handleRemovePlayerFromLineup(playerId, props.state, props.setState)
                }
            }
        },
        formatter: (cell: any, row: any) =>
            row.name && <img data-player-id={row.playerId} src={removeIcon} alt={"remove"}/>
    }, {
        dataField: 'displayPosition',
        text: 'Position',
        footer: '',
    }, {
        dataField: 'name',
        text: 'Player',
        footer: 'Total',
        formatter: (cellContent: any, row: any, index: number) => <LineupPlayerCell key={index} player={row}/>
    }, {
        dataField: 'projection',
        text: 'Projection',
        footer: pointSum.toFixed(1),
        formatter: (cellContent: any, row: any) => <p>{row.projection && row.projection.toFixed(1)}</p>
    }, {
        dataField: 'salary',
        text: 'Salary',
        footer: getFormattedSalary(salarySum),
        footerStyle: salaryStyle,
        formatter: (cellContent: any, row: any) =>
            <p>{site === 'DraftKings' ?
                getAdjustedDraftKingsSalary(row.salary, row.displayPosition) : getFormattedSalary(row.salary)}</p>
    }];

    return <BootstrapTable keyField='lineupIndex'
                           data={lineup}
                           columns={columns}
                           headerWrapperClasses={lineupHeaderClass}
                           rowClasses="Player-row"
                           rowStyle={rowStyle}
    />
}