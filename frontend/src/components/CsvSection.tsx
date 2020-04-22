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
    const shouldRenderElement = lineup.length > 0 && lineup.every((player: any) => player.name.length > 0);

    if (shouldRenderElement) {
        return (
            <CSVLink data={csvData} filename={site + '-' + sport + '-lineup.csv'}>Download Lineup CSV</CSVLink>

        );
    } else
        return null;
};
