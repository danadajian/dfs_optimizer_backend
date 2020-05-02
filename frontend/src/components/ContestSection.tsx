import React from "react";
import '../css/ContestSection.css'
import {State} from "../interfaces";
import {handleContestChange} from "../handlers/handleContestChange";
import {getButtonStyle} from "../helpers/getButtonStyle/getButtonStyle";

export const ContestSection = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {isLoading, site, sport, contest, contests} = props.state;
    const shouldRenderElement = !isLoading && site && sport;

    const element =
        contests.length === 0 ?
            <p>No contests are available.</p> :
            <div>
                <h3>Choose a contest:</h3>
                <div className="Contest-section">
                    {contests.map(
                        contestName =>
                            <button style={getButtonStyle(contest, contestName)}
                                    key={contestName}
                                    onClick={() => handleContestChange(contestName, props.state, props.setState)}>
                                {contestName}
                            </button>
                    )}
                </div>
            </div>

    return <div>{shouldRenderElement && element}</div>
};
