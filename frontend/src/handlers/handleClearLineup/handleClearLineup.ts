import {createEmptyLineup} from "../../helpers/createEmptyLineup/createEmptyLineup";
import {State} from "../../interfaces";

export const handleClearLineup = (state: State, setState: (state: State) => void) => {
    console.log(state.lineup[0]);
    setState({
        ...state,
        lineup: createEmptyLineup(state.lineupPositions, state.displayMatrix),
        whiteList: [],
        blackList: []
    });
};