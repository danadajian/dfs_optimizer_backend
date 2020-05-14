import {playerPoolAttributes, State} from "../../interfaces";

export const handleAddPlayerToLineup = (playerIdToAdd: number, state: State, setState: (state: State) => void) => {
    const {playerPool, lineup, whiteList, blackList} = state;
    const playerIsAlreadyInLineup = lineup.find((player: any) => player.playerId === playerIdToAdd);
    if (playerIsAlreadyInLineup) {
        alert('Player already added to lineup.');
        return
    }
    const playerToAdd = playerPool
        .find((player: playerPoolAttributes) => player.playerId === playerIdToAdd)!;
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
        alert('Not enough positions available to add player.');
        return
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
        playerToAddCopy.lineupIndex = lineupIndex;
        lineup[lineupIndex] = playerToAddCopy;
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
