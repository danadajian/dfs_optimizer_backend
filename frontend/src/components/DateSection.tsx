import React from "react";
import DatePicker from 'react-datepicker';
import {State} from "../interfaces";
import '../css/DateSection.css'
import {handleDateChange} from "../handlers/handleDateChange/handleDateChange";

export const DateSection = (props: {
    state: State,
    setState: (state: State) => void
}) =>
    <DatePicker className="Date-section"
                selected={props.state.date}
                onChange={(newDate: any) =>
                    handleDateChange(newDate, props.state, props.setState)}/>;
