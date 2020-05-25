import React from "react";
import '../css/Loading.css'

const loading = require("../icons/loading.svg") as any;
const baseball = require("../icons/baseball.svg") as any;
const football = require("../icons/football.ico") as any;
const basketball = require("../icons/basketball.svg") as any;
const hockey = require("../icons/hockey.svg") as any;

export const Loading = (props: {
    sport: string,
    loadingText: string,
    className: string
}) => {
    const sportImageMap: any = {
        mlb: baseball,
        nfl: football,
        nba: basketball,
        nhl: hockey
    }
    return (
        <div className={props.className}>
            <p className="Loading-text">{'Loading ' + props.loadingText + ' . . .'}</p>
            <img src={sportImageMap[props.sport] || loading} className="Loading-logo" alt="Loading Logo"/>
        </div>
    )
}