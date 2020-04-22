import React from "react";

const upIcon = require("../icons/up.svg") as any;
const downIcon = require("../icons/down.svg") as any;

export const PlayerPoolGridHeader = (props: {
    sortAttribute: string,
    setSortAttribute: (sortAttribute: string) => void,
    sortSign: number,
    setSortSign: (sortSign: number) => void,
}) => {
    const {sortAttribute, setSortAttribute, sortSign, setSortSign} = props;

    const sortBy = (attribute: string) => {
        if (attribute === sortAttribute)
            setSortSign(-sortSign);
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
                <img src={sortSign === 1 ? downIcon : upIcon} alt={"sort"}
                     onClick={() => sortBy('projection')}
                     style={{
                         marginLeft: '1vmin', height: '2vmin',
                         backgroundColor: sortAttribute === 'projection' ? 'red' : 'white'
                     }}/>
            </th>
            <th>Salary
                <img src={sortSign === 1 ? downIcon : upIcon} alt={"sort"}
                     onClick={() => sortBy('salary')}
                     style={{
                         marginLeft: '1vmin', height: '2vmin',
                         backgroundColor: sortAttribute === 'salary' ? 'red' : 'white'
                     }}/>
            </th>
            <th>$/Point
                <img src={sortSign === 1 ? downIcon : upIcon} alt={"sort"}
                     onClick={() => sortBy('pricePerPoint')}
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
