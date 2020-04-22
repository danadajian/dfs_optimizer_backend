import React from "react";

const baseball = require("../icons/baseball2.svg") as any;
const football = require("../icons/football2.svg") as any;
const basketball = require("../icons/basketball2.svg") as any;
const hockey = require("../icons/hockey2.svg") as any;

export const Optimizing = (props: {
    sport: string
}) => {
    return (
        <div className={"Loading"}>
            <div><p className={"Optimizing-text"}>Optimizing . . .</p></div>
            <div><img src={
                props.sport === 'mlb' ? baseball :
                    props.sport === 'nfl' ? football :
                        props.sport === 'nba' ? basketball :
                            props.sport === 'nhl' ? hockey :
                                football
            } className={"App-logo2"} alt="football2"/></div>
        </div>
    )
};