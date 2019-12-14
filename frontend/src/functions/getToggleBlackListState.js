function getToggleBlackListState(playerIndex, state) {
    let {playerPool, lineup, whiteList, blackList, lineupPositions, displayMatrix} = state;
    let blackListedPlayer = playerPool[playerIndex];
    if (blackList.includes(blackListedPlayer.playerId)) {
        blackList.splice(blackList.indexOf(blackListedPlayer.playerId), 1);
    } else {
        blackList.push(blackListedPlayer.playerId);
        if (whiteList.includes(blackListedPlayer.playerId)) {
            whiteList.splice(whiteList.indexOf(blackListedPlayer.playerId), 1);
        }
        let playerInLineup = lineup.find((player) => player.playerId === blackListedPlayer.playerId, null);
        if (playerInLineup) {
            let lineupIndex = lineup.indexOf(playerInLineup);
            lineup[lineupIndex] = {
                position: lineupPositions[lineupIndex],
                displayPosition: displayMatrix[lineupIndex],
                team: '',
                name: '',
                playerId: '',
                projected: '',
                salary: '',
                opponent: ''
            };
        }
    }
    return {
        lineup: lineup,
        whiteList: whiteList,
        blackList: blackList,
        filteredPool: null,
        searchText: ''
    }
}

export { getToggleBlackListState }