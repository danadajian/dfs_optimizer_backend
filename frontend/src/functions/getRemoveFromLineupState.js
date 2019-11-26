function getRemoveFromLineupState(lineupIndex, state) {
    let {lineup, whiteList, lineupMatrix, displayMatrix} = state;
    let playerToRemove = lineup[lineupIndex];
    let playerInWhiteList = whiteList.find((playerId) => playerId === playerToRemove.playerId, null);
    if (playerInWhiteList) {
        whiteList.splice(whiteList.indexOf(playerInWhiteList), 1);
    }
    lineup[lineupIndex] = {
        position: lineupMatrix[lineupIndex],
        displayPosition: displayMatrix[lineupIndex],
        team: '',
        name: '',
        playerId: '',
        projected: '',
        salary: '',
        opponent: ''
    };
    return {
        lineup: lineup,
        whiteList: whiteList
    }
}

export { getRemoveFromLineupState }