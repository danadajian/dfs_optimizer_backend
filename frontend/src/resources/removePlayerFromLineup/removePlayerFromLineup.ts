export const removePlayerFromLineup = (lineupIndex: number, lineup: any[], whiteList: any[],
                                       lineupPositions: string[], displayMatrix: string[]) => {
    let playerToRemove = lineup[lineupIndex];
    let playerInWhiteList = whiteList.find((playerId: number) => playerId === playerToRemove.playerId, null);
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
};