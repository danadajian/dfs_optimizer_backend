import React, {useRef, useState} from "react";
import Button from 'react-bootstrap/Button';
import '../css/ActionButtonSection.css'
import {lineupAttributes, StateProps} from "../interfaces";
import {handleGenerateOptimalLineup} from "../handlers/handleGenerateOptimalLineup/handleGenerateOptimalLineup";
import {handleClearLineup} from "../handlers/handleClearLineup/handleClearLineup";
import {handleExportLineup} from "../handlers/handleExportLineup/handleExportLineup";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import Tooltip from "react-bootstrap/Tooltip";
import {Lineup} from "./Lineup";
import {Optimizing} from "./Optimizing";

export const ActionButtonSection = (props: StateProps) => {
    const {isOptimizing, site, sport, contest, lineup} = props.state;
    const shouldRenderElement = sport && contest && site;

    const [shouldRenderLineup, setShouldRenderLineup] = useState(false);
    const componentRef = useRef();

    const toolTip =
        <Tooltip id="lineup-tooltip">
            Click to show/hide lineup.
        </Tooltip>;

    const lineupButtonText = shouldRenderLineup ? 'Hide Lineup' : 'Show Lineup';

    const shouldRenderExportButton = lineup.every((player: lineupAttributes) => player.name);

    const element =
        <div className="Action-button-section">
            <Button
                variant={"dark"}
                onClick={() => handleGenerateOptimalLineup(props.state, props.setState)}>Optimize Lineup</Button>
            <Button
                variant={"secondary"}
                onClick={() => handleClearLineup(props.state, props.setState)}>Clear Lineup</Button>
            <OverlayTrigger trigger={"hover"} placement={"auto"} defaultShow={true} overlay={toolTip}>
                <Button variant={"success"}
                        onClick={() => setShouldRenderLineup(!shouldRenderLineup)}>{lineupButtonText}</Button>
            </OverlayTrigger>
            {shouldRenderExportButton &&
            <Button variant={"primary"}
                    onClick={() =>
                        handleExportLineup(setShouldRenderLineup, navigator, componentRef)}>Share Lineup</Button>}
            {shouldRenderLineup && <Lineup {...props} ref={componentRef}/>}
        </div>

    if (isOptimizing) {
        return <Optimizing sport={sport}/>
    } else {
        return <>{shouldRenderElement && element}</>
    }
};
