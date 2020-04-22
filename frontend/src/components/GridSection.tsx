import React from "react";
import {Loading} from "./Loading";
import {Optimizing} from "./Optimizing";
import {BlackList} from "./BlackList";
import {PlayerPool} from "./PlayerPool";
import {Lineup} from "./Lineup";
import {State} from "../interfaces";

export const GridSection: any = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {isLoading, isOptimizing, site, sport, contest, playerPool, blackList, loadingText,} = props.state;
    const shouldRenderElement = site && sport && contest;

    if (isLoading) {
        return <Loading sport={sport} loadingText={loadingText}/>
    } else if (isOptimizing) {
        return <Optimizing sport={sport}/>
    } else if (shouldRenderElement) {
        return (
            <div className={"Dfs-grid-section"}>
                <Lineup state={props.state} setState={props.setState}/>
                <PlayerPool state={props.state} setState={props.setState}/>
                <BlackList blackList={blackList} playerPool={playerPool}/>
            </div>
        )
    } else
        return null
};