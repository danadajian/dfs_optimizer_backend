import React from "react";
import {handleSiteChange} from "../handlers/handleSiteChange";
import {State} from "../interfaces";

export const SiteSection = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    return (
        <div style={{display: 'flex'}}>
            {
                ['Fanduel', 'DraftKings'].map((site: string, index: number) => {
                    return <button
                        key={index}
                        style={{backgroundColor: (props.state.site === site) ? 'dodgerblue' : 'white'}}
                        onClick={() => handleSiteChange(site, props.setState)}>{site}
                    </button>;
                })
            }
        </div>
    );
};
