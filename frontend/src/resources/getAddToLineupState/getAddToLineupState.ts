export const getAddToLineupState = (playerIndex: number, state: any) => {
    let {playerPool, lineup, whiteList, blackList} = state;
    let playerToAdd = playerPool[playerIndex];
    let playerInLineup = lineup.find((player: any) => player.playerId === playerToAdd.playerId, null);
    if (playerInLineup) {
        return 'Player already added to lineup.'
    }
    let spotsToReplace = lineup.filter(
        (player: any) =>
            !player.name && (
                playerToAdd.position === player.position
                || player.position.includes(playerToAdd.position)
                || playerToAdd.position.includes(player.position)
                || player.position === 'any'
            )
    );
    if (spotsToReplace.length === 0) {
        return 'Not enough positions available to add player.'
    } else {
        whiteList.push(playerToAdd.playerId);
        if (blackList.includes(playerToAdd.playerId)) {
            blackList.splice(blackList.indexOf(playerToAdd.playerId), 1);
        }
        let spotToReplace = spotsToReplace[0];
        let lineupIndex = lineup.indexOf(spotToReplace);
        let playerToAddCopy = JSON.parse(JSON.stringify(playerToAdd));
        playerToAddCopy.position = spotToReplace.position;
        playerToAddCopy.displayPosition = spotToReplace.displayPosition;
        lineup[lineupIndex] = playerToAddCopy;
        return {
            lineup: lineup,
            whiteList: whiteList,
            blackList: blackList,
            filteredPool: null,
            searchText: ''
        }
    }
};
