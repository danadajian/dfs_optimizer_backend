import football from "./resources/football.ico";
import React from "react";

export const Loading =
    <div className={"Loading"}>
        <div><p className={"Loading-text"}>Loading . . .</p></div>
        <div><img src={football} className={"App-logo"} alt="football"/></div>
    </div>;