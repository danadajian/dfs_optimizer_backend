import React from "react";
import Button from 'react-bootstrap/Button';
import '../css/ActionButtonSection.css'
import {State} from "../interfaces";
import {handleGenerateOptimalLineup} from "../handlers/handleGenerateOptimalLineup/handleGenerateOptimalLineup";
import {handleClearLineup} from "../handlers/handleClearLineup/handleClearLineup";
import {CsvSection} from "./CsvSection";

export const ActionButtonSection = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {site, sport, contest} = props.state;
    const shouldRenderElement = sport && contest && site;

    const element =
        <div className="Action-button-section">
            <Button
                variant={"success"}
                onClick={() => handleGenerateOptimalLineup(props.state, props.setState)}>Optimize Lineup</Button>
            <Button
                variant={"secondary"}
                onClick={() => handleClearLineup(props.state, props.setState)}>Clear Lineup</Button>
            <CsvSection state={props.state}/>
        </div>

    return <div>{shouldRenderElement && element}</div>
};
