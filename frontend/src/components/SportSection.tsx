import React from "react";
import {SUPPORTED_SPORTS} from "../constants";

export const SportSection = (props: {
    isLoading: boolean,
    site: string,
    sport: string,
    setSport: (sport: string) => void
}) =>
    props.isLoading || !props.site ? null :
        <div>
            <h3>Choose a sport:</h3>
            <div style={{display: 'flex'}}>
                {SUPPORTED_SPORTS.map(
                    thisSport => <button
                        key={thisSport}
                        style={{backgroundColor: (props.sport === thisSport) ? 'dodgerblue' : 'white'}}
                        onClick={() => props.setSport(thisSport)}>{thisSport.toUpperCase()}</button>
                )}
            </div>
        </div>;