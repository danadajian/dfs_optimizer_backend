import React from "react";
import OverlayTrigger from 'react-bootstrap/OverlayTrigger'
import Tooltip from 'react-bootstrap/Tooltip'
import ButtonGroup from 'react-bootstrap/ButtonGroup'
import Button from 'react-bootstrap/Button'
import {handleSiteChange} from "../handlers/handleSiteChange/handleSiteChange";
import {State} from "../interfaces";

export const SiteSection = (props: {
    state: State,
    setState: (state: State) => void,
    isDesktopView: boolean
}) => {
    const shouldDisplayTooltip = !props.state.site;
    const buttonGroup = (
        <ButtonGroup className="ml-2 mr-2 mt-1 mb-1">
            <Button variant="outline-primary"
                    active={props.state.site === 'Fanduel'}
                    onClick={() => handleSiteChange('Fanduel', props.setState)}>Fanduel</Button>
            <Button variant="outline-dark"
                    active={props.state.site === 'DraftKings'}
                    onClick={() => handleSiteChange('DraftKings', props.setState)}>DraftKings</Button>
        </ButtonGroup>
    );

    if (shouldDisplayTooltip) {
        return (
            <OverlayTrigger
                placement={'bottom'}
                defaultShow={props.isDesktopView}
                overlay={
                    <Tooltip id={'site-tooltip'}>
                        Select a site to begin.
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
