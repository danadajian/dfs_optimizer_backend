import React from 'react'
import '../css/PlayerPoolGrid.css'
import {State} from "../interfaces";
import BootstrapTable from 'react-bootstrap-table-next';
import {handleAddPlayerToLineup} from "../handlers/handleAddPlayerToLineup/handleAddPlayerToLineup";
import {handleAddPlayerToBlackList} from "../handlers/handleAddPlayerToBlackList/handleAddPlayerToBlackList";
import {PlayerPoolPlayerCell} from "./PlayerPoolPlayerCell";
import {getOpponentRankStyle} from "./LineupPlayer";
import {getOrdinalString} from "../helpers/getOrdinalString/getOrdinalString";

const plusIcon = require('../icons/plus.ico');
const minusIcon = require('../icons/minus.ico');
const upIcon = require('../icons/up.svg');
const downIcon = require('../icons/down.svg');

export const PlayerPoolGrid: any = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {playerPool, filteredPool, whiteList, blackList} = props.state;

    const getSortIcon = (sortOrder: string) => {
        if (sortOrder) {
            return <img src={sortOrder === 'asc' ? upIcon : downIcon} alt={"sort"}/>
        } else {
            return <div className="Up-down-buttons">
                <img src={upIcon} alt={"up"}/>
                <img src={downIcon} alt={"down"}/>
            </div>
        }
    }

    const columns = [{
        dataField: 'add',
        text: '+',
        events: {
            onClick: (event: any, column: any, columnIndex: number, row: any, rowIndex: number) =>
                handleAddPlayerToLineup(rowIndex, props.state, props.setState)
        },
        formatter: () => <img src={plusIcon} alt={"add"}/>
    }, {
        dataField: 'blacklist',
        text: '-',
        events: {
            onClick: (event: any, column: any, columnIndex: number, row: any, rowIndex: number) =>
                handleAddPlayerToBlackList(rowIndex, props.state, props.setState)
        },
        formatter: () => <img src={minusIcon} alt={"blacklist"}/>
    }, {
        dataField: 'name',
        text: 'Player',
        formatter: (cellContent: any, row: any, index: number) => <PlayerPoolPlayerCell key={index} player={row}/>
    }, {
        dataField: 'projection',
        text: 'Projection',
        sort: true,
        sortCaret: getSortIcon,
        formatter: (cellContent: any, row: any) => <p>{row.projection.toFixed(1)}</p>
    }, {
        dataField: 'salary',
        text: 'Salary',
        sort: true,
        sortCaret: getSortIcon,
        formatter: (cellContent: any, row: any) =>
            <p>${row.salary.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}</p>
    }, {
        dataField: 'pricePerPoint',
        text: '$/Point',
        sort: true,
        sortCaret: (order: any) => getSortIcon(order),
        sortValue: (cell: any, row: any) => row.salary / row.projection,
        formatter: (cellContent: any, row: any) =>
            <p>
                ${(row.salary / row.projection)
                .toFixed(0)
                .toString()
                .replace(/\B(?=(\d{3})+(?!\d))/g, ",")}
            </p>
    }, {
        dataField: 'opponent',
        text: 'Opponent',
        formatter: (cellContent: any, row: any) =>
            <p>
                {row.opponent + ' '}
                <b style={getOpponentRankStyle(row.opponentRank)}>
                    {getOrdinalString(row.opponentRank)}
                </b>
            </p>
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

    const rowStyle = (row: any) => ({
        backgroundColor: (whiteList.includes(row.playerId)) ? 'lightgreen' :
            (blackList.includes(row.playerId)) ? 'indianred' : 'white'
    });

    return (
        <div className="Player-pool-grid">
            <BootstrapTable keyField='playerId'
                            data={filteredPool.length > 0 ? filteredPool : playerPool}
                            columns={columns}
                            classes="Player-table"
                            headerWrapperClasses="Player-pool-grid-header"
                            rowClasses="Player-pool-row"
                            rowStyle={rowStyle}
            />
        </div>
    )
};
