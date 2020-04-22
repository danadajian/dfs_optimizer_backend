import React from "react";
import DatePicker from 'react-date-picker';
import {State} from "../interfaces";
import {handleDateChange} from "../handlers/handleDateChange";

export const DateSection = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {site, date} = props.state;
    const shouldRenderElement = site && date;

    if (shouldRenderElement) {
        return (
            <div>
                <h3>Choose a date:</h3>
                <DatePicker onChange={(newDate: any) =>
                    handleDateChange(newDate, props.state, props.setState)} value={date}/>
            </div>
        );
    } else
        return null;
};
