import React from "react";
import ButtonGroup from 'react-bootstrap/ButtonGroup'
import Button from 'react-bootstrap/Button'
import {handleSiteChange} from "../handlers/handleSiteChange/handleSiteChange";
import {State} from "../interfaces";

export const SiteSection = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    return (
        <ButtonGroup className="ml-2 mr-2 mt-1 mb-1">
            <Button variant="outline-primary"
                    active={props.state.site === 'Fanduel'}
                    onClick={() => handleSiteChange('Fanduel', props.setState)}>Fanduel</Button>
            <Button variant="outline-dark"
                    active={props.state.site === 'DraftKings'}
                    onClick={() => handleSiteChange('DraftKings', props.setState)}>DraftKings</Button>
        </ButtonGroup>
    );
};
