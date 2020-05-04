import React from "react";
import '../css/ActionButtonSection.css'
import {State} from "../interfaces";
import {handleGenerateOptimalLineup} from "../handlers/handleGenerateOptimalLineup/handleGenerateOptimalLineup";
import {handleClearLineup} from "../handlers/handleClearLineup/handleClearLineup";

export const ActionButtonSection = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {site, sport, contest} = props.state;
    const shouldRenderElement = sport && contest && site;

    const element =
        <div className="Action-button-section">
            <button onClick={() => handleGenerateOptimalLineup(props.state, props.setState)}>Optimize
                Lineup
            </button>
            <button onClick={() => handleClearLineup(props.state, props.setState)}>Clear Lineup</button>
        </div>

    return <div>{shouldRenderElement && element}</div>
};
