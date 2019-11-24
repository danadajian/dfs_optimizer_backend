function getRemoveFromLineupState(lineupIndex, state) {
    let {lineup, whiteList} = state;
    let playerToRemove = lineup[lineupIndex];
    let playerInWhiteList = whiteList.find((player) => player.playerId === playerToRemove.playerId, null);
    if (playerInWhiteList) {
        whiteList.splice(whiteList.indexOf(playerInWhiteList), 1);
    }
    lineup[lineupIndex] = {
        position: playerToRemove.position,
        displayPosition: playerToRemove.displayPosition,
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