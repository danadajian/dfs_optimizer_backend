import React from "react";
import '../css/SearchBar.css'
import {State} from "../interfaces";
import {handleFilterPlayers} from "../handlers/handleFilterPlayers/handleFilterPlayers";

const search = require("../icons/search.ico") as any;

export const SearchBar: any = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    return (
        <div className="Search-bar">
            {!props.state.searchText && <img src={search} alt="search"/>}
            <input type="text"
                   value={props.state.searchText}
                   onChange={(event: any) =>
                       handleFilterPlayers('name', event.target.value, props.state, props.setState)}>{}</input>
        </div>
    )
};