import React from "react";
import DatePicker from 'react-date-picker';

export const DateSection = (props: {
    date: Date,
    handleDateChange: (date: Date) => void
}): any => {
    return (
        <div>
            <h3>Choose a date:</h3>
            <DatePicker onChange={(newDate: any) => props.handleDateChange(newDate)} value={props.date}/>
        </div>
    );
};
