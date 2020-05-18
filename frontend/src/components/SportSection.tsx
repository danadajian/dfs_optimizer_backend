import React from "react";
import ButtonGroup from 'react-bootstrap/ButtonGroup'
import Button from 'react-bootstrap/Button'
import {SUPPORTED_SPORTS} from "../constants";
import {StateProps} from "../interfaces";
import {handleSportChange} from "../handlers/handleSportChange/handleSportChange";
import Tooltip from "react-bootstrap/Tooltip";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";

export const SportSection = (props: StateProps) => {
    const {site, sport} = props.state;
    const shouldDisplayTooltip = site && !sport;

    const buttonGroup =
        <ButtonGroup className="ml-2 mr-2 mt-1 mb-1">
            {SUPPORTED_SPORTS.map(
                supportedSport =>
                    <Button
                        key={supportedSport}
                        variant={site === 'Fanduel' ? "outline-primary" : "outline-dark"}
                        active={sport === supportedSport}
                        disabled={!site}
                        onClick={() => handleSportChange(supportedSport, props.state, props.setState)}>
                        {supportedSport.toUpperCase()}
                    </Button>
            )}
        </ButtonGroup>;

    if (shouldDisplayTooltip) {
        return (
            <OverlayTrigger
                placement={'bottom'}
                defaultShow
                overlay={
                    <Tooltip id={'sport-tooltip'}>
                        Select a sport.
                    </Tooltip>
                }
            >
                {buttonGroup}
            </OverlayTrigger>
        )
    } else {
        return buttonGroup
    }
};