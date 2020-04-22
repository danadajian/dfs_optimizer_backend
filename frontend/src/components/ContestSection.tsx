import React from "react";
import {State} from "../State";

export const ContestSection = (props: {
    state: State,
    setState: (state: State) => void,
    handleContestChange: (contest: string, state: State, setState: (state: State) => void) => void
}) => {
    const {contest, contests} = props.state;
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
                                    onClick={() => props.handleContestChange(contestName, props.state, props.setState)}>{contestName}</button>
                    )}
                </div>
            </div>
    )
};
