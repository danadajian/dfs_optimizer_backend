import {State} from "../../interfaces";

export const handleDateChange = (date: Date, state: State, setState: (state: State) => void) => {
    setState({
        ...state,
        date
    })
};