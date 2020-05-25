import React from "react";
import '../css/GridSection.css'
import {PlayerPool} from "./PlayerPool";
import {StateProps} from "../interfaces";
import {ActionButtonSection} from "./ActionButtonSection";

export const GridSection: any = (props: StateProps) => {
    const {site, sport} = props.state;
    const shouldRenderElement = site && sport;

    const element =
        <div className="Grid-section">
                <span>
                    <ActionButtonSection {...props}/>
                </span>
            <PlayerPool {...props}/>
        </div>

    return <>{shouldRenderElement && element}</>
};