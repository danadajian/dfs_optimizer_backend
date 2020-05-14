import React from 'react';
import {State} from "../interfaces";
import '../css/Lineup.css'
import '../css/LineupPlayer.css'
import {handleRemovePlayerFromLineup} from "../handlers/handleRemovePlayerFromLineup/handleRemovePlayerFromLineup";
import {sumAttribute} from "../helpers/sumAttribute/sumAttribute";
import BootstrapTable from 'react-bootstrap-table-next';
import Button from "react-bootstrap/Button";
import Popover from "react-bootstrap/Popover"
import OverlayTrigger from "react-bootstrap/OverlayTrigger"
import {LineupPlayerCell} from "./LineupPlayerCell";
import {getFormattedSalary} from "../helpers/getFormattedSalary/getFormattedSalary";
import {getAdjustedDraftKingsSalary} from "../helpers/getAdjustedDraftKingsSalary/getAdjustedDraftKingsSalary";

export const Lineup = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {site, lineup, whiteList, salaryCap} = props.state;

    const pointSum = sumAttribute(lineup, 'projection');
    const salarySum = sumAttribute(lineup, 'salary');

    const lineupHeaderClass = (site === 'Fanduel') ? "Fanduel-header" : "DraftKings-header";

    const salaryStyle = {
        color: (salarySum > salaryCap) ? 'indianred' : 'black'
    };

    const columns = [{
        dataField: 'remove',
        text: '',
        footer: '',
        formatter: (cellContent: any, row: any, index: number) => {
            return (
                <span>
                    {row.position && row.name &&
                    <Button variant={"danger"}
                            size={"sm"}
                            className="Remove-button"
                            onClick={() => handleRemovePlayerFromLineup(index, props.state, props.setState)}>X</Button>}
                </span>
            )
        }
    }, {
        dataField: 'displayPosition',
        text: 'Position',
        footer: '',
    }, {
        dataField: 'name',
        text: 'Player',
        isDummyField: true,
        footer: 'Total',
        formatter: (cellContent: any, row: any, index: number) => <LineupPlayerCell key={index} player={row}/>
    }, {
        dataField: 'projection',
        text: 'Projection',
        isDummyField: true,
        footer: pointSum.toFixed(1),
        formatter: (cellContent: any, row: any) => <p>{row.projection && row.projection.toFixed(1)}</p>
    }, {
        dataField: 'salary',
        text: 'Salary',
        isDummyField: true,
        footer: getFormattedSalary(salarySum),
        footerStyle: salaryStyle,
        formatter: (cellContent: any, row: any) =>
            <p>{site === 'DraftKings' ?
                getAdjustedDraftKingsSalary(row.salary, row.displayPosition) : getFormattedSalary(row.salary)}</p>
    }];

    const rowStyle = (row: any) => ({
        backgroundColor: whiteList.includes(row.playerId) ? 'lightgreen' : 'white'
    });

    const lineupPopover = (
        <Popover id="lineup-popover" className="mw-100 mh-100">
            <Popover.Content>
                <BootstrapTable keyField='lineupIndex'
                                data={lineup}
                                columns={columns}
                                headerWrapperClasses={lineupHeaderClass}
                                rowClasses="Player-row"
                                rowStyle={rowStyle}
                />
            </Popover.Content>
        </Popover>
    );

    return (
        <OverlayTrigger trigger="click" placement="auto" overlay={lineupPopover}>
            <Button variant="success">View Lineup</Button>
        </OverlayTrigger>
    )
};
