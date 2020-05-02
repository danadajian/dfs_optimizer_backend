import React from "react";
import '../css/PlayerPoolGridHeader.css'

const upIcon = require("../icons/up.svg") as any;
const downIcon = require("../icons/down.svg") as any;

export const PlayerPoolGridHeader = (props: {
    sortAttribute: string,
    setSortAttribute: (sortAttribute: string) => void,
    isAscendingSort: boolean,
    setSortSign: (isAscendingSort: boolean) => void,
}) => {
    const {sortAttribute, setSortAttribute, isAscendingSort, setSortSign} = props;

    const sortByAttribute = (attribute: string) => {
        if (attribute === sortAttribute)
            setSortSign(!isAscendingSort);
        else
            setSortAttribute(attribute);
    };

    return (
        <thead>
        <tr className="Player-pool-grid-header">
            <th>{}</th>
            <th>{}</th>
            <th>Player</th>
            <th>Projection
                <img src={isAscendingSort ? upIcon : downIcon} alt={"sort"}
                     onClick={() => sortByAttribute('projection')}
                     style={{backgroundColor: sortAttribute === 'projection' ? 'red' : 'white'}}/>
            </th>
            <th>Salary
                <img src={isAscendingSort ? upIcon : downIcon} alt={"sort"}
                     onClick={() => sortByAttribute('salary')}
                     style={{backgroundColor: sortAttribute === 'salary' ? 'red' : 'white'}}/>
            </th>
            <th>$/Point
                <img src={isAscendingSort ? upIcon : downIcon} alt={"sort"}
                     onClick={() => sortByAttribute('pricePerPoint')}
                     style={{backgroundColor: sortAttribute === 'pricePerPoint' ? 'red' : 'white'}}/>
            </th>
            <th>Opponent</th>
            <th>Spread</th>
            <th>O/U</th>
            <th>Game Date</th>
        </tr>
        </thead>
    );
};
