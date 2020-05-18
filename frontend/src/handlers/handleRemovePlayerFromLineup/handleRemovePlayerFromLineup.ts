import {lineupAttributes, State} from "../../interfaces";

export const handleRemovePlayerFromLineup = (playerIdToRemove: number, state: State, setState: (state: State) => void) => {
    const {lineup, whiteList, lineupPositions, displayMatrix} = state;
    const playerToRemove = lineup.find((player: lineupAttributes) => player.playerId === playerIdToRemove)!;
    whiteList.splice(whiteList.indexOf(playerIdToRemove), 1);
    const lineupIndex = playerToRemove.lineupIndex;
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
        filteredPool: [],
        sortValue: 'All'
    })
};