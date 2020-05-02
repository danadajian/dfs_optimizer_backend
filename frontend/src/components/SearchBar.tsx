import React from "react";
import '../css/SearchBar.css'
import {playerPoolAttributes} from "../interfaces";

const search = require("../icons/search.ico") as any;

export const SearchBar: any = (props: {
    searchText: string,
    filteredPool: playerPoolAttributes[]
    handleFilter: (attribute: string, value: string) => void
}) => {
    return (
        <div className="Search-bar">
            {!props.filteredPool && <img src={search} alt="search"/>}
            <input type="text"
                   value={props.searchText}
                   onChange={(event) =>
                       props.handleFilter('name', event.target.value)}>{null}</input>
        </div>
    )
};