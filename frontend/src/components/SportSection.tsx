import React from "react";
import '../css/SportSection.css'
import {SUPPORTED_SPORTS} from "../constants";
import {State} from "../interfaces";
import {handleSportChange} from "../handlers/handleSportChange/handleSportChange";
import {getButtonStyle} from "../helpers/getButtonStyle/getButtonStyle";

export const SportSection = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {isLoading, site, sport} = props.state;
    const shouldRenderElement = !isLoading && site;

    const element =
        <div>
            <h3>Choose a sport:</h3>
            <div className="Sport-section">
                {SUPPORTED_SPORTS.map(
                    supportedSport =>
                        <button
                            key={supportedSport}
                            style={getButtonStyle(sport, supportedSport)}
                            onClick={() => handleSportChange(supportedSport, props.state, props.setState)}>
                            {supportedSport.toUpperCase()}
                        </button>
                )}
            </div>
        </div>

    return <div>{shouldRenderElement && element}</div>
};