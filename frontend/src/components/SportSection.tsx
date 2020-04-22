import React from "react";
import {SUPPORTED_SPORTS} from "../constants";
import {State} from "../interfaces";
import {handleSportChange} from "../handlers/handleSportChange";

export const SportSection = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {isLoading, site, sport} = props.state;
    const shouldRenderElement = !isLoading && site;

    if (shouldRenderElement) {
        return (
            <div>
                <h3>Choose a sport:</h3>
                <div style={{display: 'flex'}}>
                    {SUPPORTED_SPORTS.map(
                        supportedSport =>
                            <button
                                key={supportedSport}
                                style={{backgroundColor: (sport === supportedSport) ? 'dodgerblue' : 'white'}}
                                onClick={() => handleSportChange(supportedSport, props.state, props.setState)}>
                                {supportedSport.toUpperCase()}
                            </button>
                    )}
                </div>
            </div>
        );
    } else
        return null
};