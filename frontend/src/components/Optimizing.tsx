import React from "react";
import '../css/Optimizing.css'

const baseball = require("../icons/baseball2.svg") as any;
const football = require("../icons/football2.svg") as any;
const basketball = require("../icons/basketball2.svg") as any;
const hockey = require("../icons/hockey2.svg") as any;

export const Optimizing = (props: {
    sport: string
}) => {
    const sportImageMap: any = {
        mlb: baseball,
        nfl: football,
        nba: basketball,
        nhl: hockey
    }
    return (
        <div className="Loading">
            <div><p className="Optimizing-text">Optimizing . . .</p></div>
            <div><img src={sportImageMap[props.sport]} className="Optimizing-logo" alt="Optimizing Image"/></div>
        </div>
    )
};