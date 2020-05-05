import React from "react";
import ButtonGroup from 'react-bootstrap/ButtonGroup'
import Button from 'react-bootstrap/Button'
import {SUPPORTED_SPORTS} from "../constants";
import {State} from "../interfaces";
import {handleSportChange} from "../handlers/handleSportChange/handleSportChange";

export const SportSection = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {site, sport} = props.state;

    return (
        <ButtonGroup className="ml-2 mr-2 mt-1 mb-1">
            {SUPPORTED_SPORTS.map(
                supportedSport =>
                    <Button
                        key={supportedSport}
                        variant={site === 'Fanduel' ? "outline-primary" : "outline-dark"}
                        active={sport === supportedSport}
                        onClick={() => handleSportChange(supportedSport, props.state, props.setState)}>
                        {supportedSport.toUpperCase()}
                    </Button>
            )}
        </ButtonGroup>
    )
};