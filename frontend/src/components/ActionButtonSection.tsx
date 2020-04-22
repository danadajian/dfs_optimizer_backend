import React from "react";
import {State} from "../interfaces";
import {handleGenerateOptimalLineup} from "../handlers/handleGenerateOptimalLineup";
import {handleClearLineup} from "../handlers/handleClearLineup";

export const ActionButtonSection = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {site, sport, contest} = props.state;
    const shouldRenderElement = sport && contest && site;

    if (shouldRenderElement) {
        return (
            <div style={{display: 'flex', margin: '2%'}}>
                <button style={{marginTop: '10px'}}
                        onClick={() => handleGenerateOptimalLineup(props.state, props.setState)}>Optimize
                    Lineup</button>
                <button style={{marginTop: '10px'}}
                        onClick={() => handleClearLineup(props.state, props.setState)}>Clear Lineup</button>
            </div>
        )
    } else
        return null
};
