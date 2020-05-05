import React from "react";
import {State} from "../interfaces";
import {CSVLink} from "react-csv";

export const CsvSection = (props: {
    state: State
}) => {
    const {site, sport, lineup, displayMatrix} = props.state;
    const csvData = [
        displayMatrix,
        lineup.map((player: any) => player.name)
    ];
    const shouldRenderElement = lineup.length > 0 && lineup.every((player: any) => player.name);

    const element = <CSVLink data={csvData}
                             className="btn btn-info"
                             filename={`Optimal ${site} ${sport} Lineup.csv`}>Download Lineup CSV</CSVLink>

    return <div>{shouldRenderElement && element}</div>
};
