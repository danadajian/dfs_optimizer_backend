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
        <div className="Optimizing">
            <p>Optimizing . . .</p>
            <img src={sportImageMap[props.sport]} alt="Optimizing Logo"/>
        </div>
    )
};