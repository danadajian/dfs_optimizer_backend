import React from "react";
import {SUPPORTED_SPORTS} from "../constants";

export const SportSection = (props: {
    sport: string,
    handleSportChange: (sport: string) => void
}) => {
    return (
        <div>
            <h3>Choose a sport:</h3>
            <div style={{display: 'flex'}}>
                {SUPPORTED_SPORTS.map(
                    supportedSport =>
                        <button
                            key={supportedSport}
                            style={{backgroundColor: (props.sport === supportedSport) ? 'dodgerblue' : 'white'}}
                            onClick={() => props.handleSportChange(supportedSport)}
                        >
                            {supportedSport.toUpperCase()}
                        </button>
                )}
            </div>
        </div>
    );
};