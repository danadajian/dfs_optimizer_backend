import React from "react";

const sports = ['mlb', 'nfl', 'nba', 'nhl'];

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
                {sports.map(
                    thisSport => <button
                        key={thisSport}
                        style={{backgroundColor: (props.sport === thisSport) ? 'dodgerblue' : 'white'}}
                        onClick={() => props.setSport(thisSport)}>{thisSport.toUpperCase()}</button>
                )}
            </div>
        </div>;