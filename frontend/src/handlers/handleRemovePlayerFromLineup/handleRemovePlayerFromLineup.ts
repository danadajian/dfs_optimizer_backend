import {State} from "../../interfaces";

export const handleRemovePlayerFromLineup = (lineupIndex: number, state: State, setState: (state: State) => void) => {
    const {lineup, whiteList, lineupPositions, displayMatrix} = state;
    let playerToRemove = lineup[lineupIndex];
    let playerInWhiteList = whiteList.find((playerId: number) => playerId === playerToRemove.playerId);
    if (playerInWhiteList) {
        whiteList.splice(whiteList.indexOf(playerInWhiteList), 1);
    }
    lineup[lineupIndex] = {
        lineupIndex,
        playerId: 0,
        position: lineupPositions[lineupIndex],
        displayPosition: displayMatrix[lineupIndex],
    };
    setState({
        ...state,
        lineup,
        whiteList,
        searchText: '',
        filteredPool: []
    })
};