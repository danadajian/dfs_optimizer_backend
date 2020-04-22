import React from "react";
import {State} from "../interfaces";
import {handleGenerateOptimalLineup} from "../handlers/handleGenerateOptimalLineup";
import {handleClearLineup} from "../handlers/handleClearLineup";

export const ActionButtonSection = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {site, sport, contest} = props.state;
    return (
        <div style={{display: 'flex', margin: '2%'}}>
            {sport && contest && site &&
            <button style={{marginTop: '10px'}}
                    onClick={() => handleGenerateOptimalLineup(props.state, props.setState)}>Optimize Lineup</button>}
            {sport && contest && site &&
            <button style={{marginTop: '10px'}}
                    onClick={() => handleClearLineup(props.state, props.setState)}>Clear Lineup</button>}
        </div>
    )
};
