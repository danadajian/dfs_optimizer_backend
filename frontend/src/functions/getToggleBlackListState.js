function getToggleBlackListState(playerIndex, state) {
    let {playerPool, lineup, whiteList, blackList} = state;
    let blackListedPlayer = playerPool[playerIndex];
    let playerInBlackList = blackList.find((player) => player.playerId === blackListedPlayer.playerId, null);
    if (playerInBlackList) {
        blackList.splice(blackList.indexOf(playerInBlackList), 1);
    } else {
        blackList.push(blackListedPlayer);
        let playerInWhiteList = whiteList.find((player) => player.playerId === blackListedPlayer.playerId, null);
        if (playerInWhiteList) {
            whiteList.splice(whiteList.indexOf(playerInWhiteList), 1);
        }
        let playerInLineup = lineup.find((player) => player.playerId === blackListedPlayer.playerId, null);
        if (playerInLineup) {
            lineup[lineup.indexOf(playerInLineup)] = {
                position: playerInLineup.position,
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