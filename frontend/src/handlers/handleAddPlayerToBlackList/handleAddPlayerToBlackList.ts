import {State} from "../../interfaces";

export const handleAddPlayerToBlackList = (playerIndex: number, state: State, setState: (state: State) => void) => {
    const {playerPool, filteredPool, lineup, whiteList, blackList, lineupPositions, displayMatrix} = state;
    const displayPlayerPool = filteredPool.length > 0 ? filteredPool : playerPool;
    let blackListedPlayer = displayPlayerPool[playerIndex];
    if (blackList.includes(blackListedPlayer.playerId)) {
        blackList.splice(blackList.indexOf(blackListedPlayer.playerId), 1);
    } else {
        blackList.push(blackListedPlayer.playerId);
        if (whiteList.includes(blackListedPlayer.playerId)) {
            whiteList.splice(whiteList.indexOf(blackListedPlayer.playerId), 1);
        }
        let playerInLineup = lineup.find((player: any) => player.playerId === blackListedPlayer.playerId, null);
        if (playerInLineup) {
            let lineupIndex = lineup.indexOf(playerInLineup);
            lineup[lineupIndex] = {
                lineupIndex,
                playerId: 0,
                position: lineupPositions[lineupIndex],
                displayPosition: displayMatrix[lineupIndex],
            };
        }
    }
    setState({
        ...state,
        lineup,
        whiteList,
        blackList,
        searchText: '',
        filteredPool: [],
        sortValue: 'All'
    })
};