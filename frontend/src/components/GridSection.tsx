import React from "react";
import '../css/GridSection.css'
import {Loading} from "./Loading";
import {Optimizing} from "./Optimizing";
import {PlayerPool} from "./PlayerPool";
import {State} from "../interfaces";
import {ActionButtonSection} from "./ActionButtonSection";

export const GridSection: any = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {isLoading, isOptimizing, site, sport, contest, loadingText} = props.state;
    const shouldRenderElement = site && sport && contest;

    if (isLoading) {
        return <Loading sport={sport} loadingText={loadingText}/>
    } else if (isOptimizing) {
        return <Optimizing sport={sport}/>
    } else {
        const element =
            <div className="Grid-section">
                <span>
                    <ActionButtonSection state={props.state} setState={props.setState}/>
                </span>
                <PlayerPool state={props.state} setState={props.setState}/>
            </div>
        return <div>{shouldRenderElement && element}</div>
    }
};