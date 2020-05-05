import {State} from "../../interfaces";

export const handleFilterPlayers = (attribute: string, sortValue: string, state: State,
                                    setState: (state: State) => void) => {
    const {playerPool} = state;
    let filteredPool;
    let searchText: string = '';
    if (sortValue === 'All') {
        filteredPool = playerPool;
    } else if (attribute === 'name') {
        searchText = sortValue.toLowerCase();
        filteredPool = playerPool.filter(
            (player: any) => player.name.toLowerCase().includes(searchText.toLowerCase())
        );
    } else {
        filteredPool = playerPool.filter(
            (player: any) => player[attribute].includes(sortValue)
        );
    }
    setState({
        ...state,
        sortValue,
        searchText,
        filteredPool
    })
};