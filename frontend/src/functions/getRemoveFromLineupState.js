export function getRemoveFromLineupState(lineupIndex, state) {
    let {lineup, whiteList, lineupPositions, displayMatrix} = state;
    let playerToRemove = lineup[lineupIndex];
    let playerInWhiteList = whiteList.find((playerId) => playerId === playerToRemove.playerId, null);
    if (playerInWhiteList) {
        whiteList.splice(whiteList.indexOf(playerInWhiteList), 1);
    }
    lineup[lineupIndex] = {
        position: lineupPositions[lineupIndex],
        displayPosition: displayMatrix[lineupIndex],
        team: '',
        name: '',
        playerId: 0,
        projected: '',
        salary: '',
        opponent: ''
    };
    return {
        lineup: lineup,
        whiteList: whiteList
    }
}