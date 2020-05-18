import React from "react";
import Form from 'react-bootstrap/Form'
import {StateProps} from "../interfaces";
import {handleFilterPlayers} from "../handlers/handleFilterPlayers/handleFilterPlayers";

export const SearchBar: any = (props: StateProps) => {
    return (
        <Form.Control type="text"
                      placeholder="Search"
                      value={props.state.searchText}
                      onChange={(event: any) =>
                          handleFilterPlayers('name', event.target.value, props.state, props.setState)}/>
    )
};