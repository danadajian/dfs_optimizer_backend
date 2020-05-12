import React from 'react'
import '../css/PlayerPoolGrid.css'
import '../css/PlayerPoolPlayer.css'
import '../css/PlayerPoolGridHeader.css'
import {playerPoolAttributes, State} from "../interfaces";
import {handleAddPlayerToLineup} from "../handlers/handleAddPlayerToLineup/handleAddPlayerToLineup";
import {handleAddPlayerToBlackList} from "../handlers/handleAddPlayerToBlackList/handleAddPlayerToBlackList";
import BootstrapTable from 'react-bootstrap-table-next';
import {PlayerPoolPlayerCell} from "./PlayerPoolPlayerCell";
import {getOpponentRankStyle} from "./LineupPlayer";
import {getOrdinalString} from "../helpers/getOrdinalString/getOrdinalString";

const plusIcon = require("../icons/plus.ico");
const minusIcon = require("../icons/minus.ico");

export const PlayerPoolGrid: any = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {playerPool, searchText, filteredPool} = props.state;

    const playerPoolToDisplay = playerPool.filter((player: playerPoolAttributes) =>
        filteredPool.includes(player) || (!searchText && filteredPool.length === 0));

    const columns = [{
        dataField: 'add/remove',
        text: '+/-',
        isDummyField: true,
        formatter: (cellContent: any, row: any) => {
            return (
                <span>
                    <img src={plusIcon} alt={"add"}
                         onClick={() => handleAddPlayerToLineup(playerPoolToDisplay.indexOf(row), props.state, props.setState)}/>
                    <img src={minusIcon} alt={"remove"}
                         onClick={() => handleAddPlayerToBlackList(playerPoolToDisplay.indexOf(row), props.state, props.setState)}/>
                </span>
            )
        }
    }, {
        dataField: 'name',
        text: 'Player',
        isDummyField: true,
        formatter: (cellContent: any, row: any) => <PlayerPoolPlayerCell player={row}/>
    }, {
        dataField: 'projection',
        text: 'Projection',
        isDummyField: true,
        formatter: (cellContent: any, row: any) => <span>{row.projection.toFixed(1)}</span>
    }, {
        dataField: 'salary',
        text: 'Salary',
        isDummyField: true,
        formatter: (cellContent: any, row: any) =>
            <span>${row.salary.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}</span>
    }, {
        dataField: 'pricePerPoint',
        isDummyField: true,
        text: '$/Point',
        formatter: (cellContent: any, row: any) =>
            <span>
                ${(row.salary / row.projection)
                .toFixed(0)
                .toString()
                .replace(/\B(?=(\d{3})+(?!\d))/g, ",")}
            </span>
    }, {
        dataField: 'opponent',
        text: 'Opponent',
        isDummyField: true,
        formatter: (cellContent: any, row: any) =>
            <span>
                {row.opponent + ' '}
                <b style={getOpponentRankStyle(row.opponentRank)}>
                    {getOrdinalString(row.opponentRank)}
                </b>
            </span>
    }, {
        dataField: 'spread',
        text: 'Spread'
    }, {
        dataField: 'overUnder',
        text: 'O/U'
    }, {
        dataField: 'gameDate',
        text: 'Game Date'
    }];

    return (
        <div className="Player-pool-grid">
            <BootstrapTable keyField='id'
                            data={playerPoolToDisplay}
                            columns={columns}
                            headerWrapperClasses="Player-pool-grid-header"
                            rowClasses="Player-pool-row"/>
        </div>
    )
};
