import React from "react";
import DatePicker from 'react-date-picker';

export const DateSection: any = (props: {
    site: string,
    date: any,
    setDate: (date: any) => void
}) =>
    props.site &&
    <div>
        <h3>Choose a date:</h3>
        <DatePicker onChange={(date) => props.setDate(date)} value={props.date}/>
    </div>;
