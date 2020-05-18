import React from "react";
import '../css/GridSection.css'
import {Loading} from "./Loading";
import {PlayerPool} from "./PlayerPool";
import {StateProps} from "../interfaces";
import {ActionButtonSection} from "./ActionButtonSection";

export const GridSection: any = (props: StateProps) => {
    const {isLoading, site, sport, contest, loadingText} = props.state;
    const shouldRenderElement = site && sport && contest;

    if (isLoading) {
        return <Loading sport={sport} loadingText={loadingText}/>
    } else {
        const element =
            <div className="Grid-section">
                <span>
                    <ActionButtonSection {...props}/>
                </span>
                <PlayerPool {...props}/>
            </div>
        return <>{shouldRenderElement && element}</>
    }
};