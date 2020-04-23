import React from "react";

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
        <tr className={"Dfs-grid-header"}>
            <th>{}</th>
            <th>{}</th>
            <th>Player</th>
            <th>Projection
                <img src={isAscendingSort ? upIcon : downIcon} alt={"sort"}
                     onClick={() => sortByAttribute('projection')}
                     style={{
                         marginLeft: '1vmin', height: '2vmin',
                         backgroundColor: sortAttribute === 'projection' ? 'red' : 'white'
                     }}/>
            </th>
            <th>Salary
                <img src={isAscendingSort ? upIcon : downIcon} alt={"sort"}
                     onClick={() => sortByAttribute('salary')}
                     style={{
                         marginLeft: '1vmin', height: '2vmin',
                         backgroundColor: sortAttribute === 'salary' ? 'red' : 'white'
                     }}/>
            </th>
            <th>$/Point
                <img src={isAscendingSort ? upIcon : downIcon} alt={"sort"}
                     onClick={() => sortByAttribute('pricePerPoint')}
                     style={{
                         marginLeft: '1vmin', height: '2vmin',
                         backgroundColor: sortAttribute === 'pricePerPoint' ? 'red' : 'white'
                     }}/>
            </th>
            <th>Opponent</th>
            <th>Spread</th>
            <th>O/U</th>
            <th>Game Date</th>
        </tr>
        </thead>
    );
};
