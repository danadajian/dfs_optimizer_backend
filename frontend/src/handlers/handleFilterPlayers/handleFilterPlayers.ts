import {State} from "../../interfaces";

export const handleFilterPlayers = (attribute: string, value: string, state: State, setState: (state: State) => void) => {
    const {playerPool} = state;
    let filteredPool;
    let searchText: string = '';
    if (value === 'All') {
        filteredPool = playerPool;
    } else if (attribute === 'name') {
        searchText = value.toLowerCase();
        filteredPool = playerPool.filter(
            (player: any) => player.name.toLowerCase().includes(searchText.toLowerCase())
        );
    } else {
        filteredPool = playerPool.filter(
            (player: any) => player[attribute].includes(value)
        );
    }
    setState({
        ...state,
        searchText,
        filteredPool
    })
};