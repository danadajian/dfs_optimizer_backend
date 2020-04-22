import React from "react";
import {State} from "../interfaces";
import {handleContestChange} from "../handlers/handleContestChange";

export const ContestSection = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {isLoading, site, sport, contest, contests} = props.state;
    const shouldRenderElement = !isLoading && site && sport;

    if (shouldRenderElement) {
        return (
            contests.length === 0 ?
                <p>No contests are available.</p> :
                <div>
                    <h3>Choose a contest:</h3>
                    <div style={{display: 'flex', justifyContent: 'center'}}>
                        {contests.map(
                            contestName =>
                                <button style={{backgroundColor: (contestName === contest) ? 'dodgerblue' : 'white'}}
                                        key={contestName}
                                        onClick={() =>
                                            handleContestChange(contestName, props.state, props.setState)}>{contestName}</button>
                        )}
                    </div>
                </div>
        )
    } else
        return null;
};
