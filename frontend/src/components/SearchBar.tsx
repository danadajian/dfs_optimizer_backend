import React from "react";
import {playerPoolAttributes} from "../interfaces";

const search = require("../icons/search.ico") as any;

export const SearchBar: any = (props: {
    searchText: string,
    filteredPool: playerPoolAttributes[]
    handleFilter: (attribute: string, value: string) => void
}) => {
    return (
        <div style={{display: 'flex', flexDirection: 'column'}}>
            {!props.filteredPool && <img src={search} style={{height: '3vmin', position: 'absolute'}} alt="search"/>}
            <input type="text" style={{height: '25px', width: '90%'}}
                   value={props.searchText}
                   onChange={(event) =>
                       props.handleFilter('name', event.target.value)}>{null}</input>
        </div>
    )
};