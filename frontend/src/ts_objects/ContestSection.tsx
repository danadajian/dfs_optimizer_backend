import React from "react";

export const ContestSection = (props: {
    isLoading: boolean,
    site: string,
    sport: string,
    contest: string,
    contests: string[],
    setContest: (contest: string) => void
}) =>
    props.isLoading || !props.site || !props.sport ? null : props.contests.length === 0 ?
        <p>No contests are available.</p> :
        <div>
            <h3>Choose a contest:</h3>
            <div style={{display: 'flex'}}>
                {props.contests.map(
                    contestName =>
                        <button style={{backgroundColor: (contestName === props.contest) ? 'dodgerblue' : 'white'}}
                                key={contestName} onClick={() => props.setContest(contestName)}>{contestName}</button>
                )}
            </div>
        </div>;
