import {createEmptyLineup} from "../../helpers/createEmptyLineup/createEmptyLineup";
import {State} from "../../interfaces";

export const handleClearLineup = (state: State, setState: (state: State) => void) => {
    setState({
        ...state,
        lineup: createEmptyLineup(state.lineupPositions, state.displayMatrix),
        whiteList: [],
        blackList: []
    });
};