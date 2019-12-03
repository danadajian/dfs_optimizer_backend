import React from "react";

const baseball = require("../icons/baseball.svg") as any;
const football = require("../icons/football.ico") as any;
const basketball = require("../icons/basketball.svg") as any;
const hockey = require("../icons/hockey.svg") as any;

export const Loading = (props: {sport: string}) =>
    <div className={"Loading"}>
        <div><p className={"Loading-text"}>Loading . . .</p></div>
        <div><img src={props.sport === 'mlb' ? baseball :
            props.sport === 'nfl' ? football :
                props.sport === 'nba' ? basketball :
                    props.sport === 'nhl' ? hockey :
                        football
        } className={"App-logo"} alt="football"/></div>
    </div>;