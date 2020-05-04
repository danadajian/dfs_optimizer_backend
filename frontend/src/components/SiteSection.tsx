import React from "react";
import {handleSiteChange} from "../handlers/handleSiteChange/handleSiteChange";
import {getButtonStyle} from "../helpers/getButtonStyle/getButtonStyle";
import {State} from "../interfaces";

export const SiteSection = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    return (
        <div>
            <h3>Choose a site:</h3>
            {
                ['Fanduel', 'DraftKings'].map((site: string, index: number) => {
                    return <button
                        key={index}
                        style={getButtonStyle(props.state.site, site)}
                        onClick={() => handleSiteChange(site, props.setState)}>{site}
                    </button>;
                })
            }
        </div>
    );
};
