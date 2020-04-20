export const getFilterPlayersState = (attribute: string, value: string, state: any) => {
    let {playerPool, text} = state;
    let filteredPool;
    if (value === 'All') {
        return {
            searchText: '',
            filteredPool: playerPool
        }
    } else if (attribute === 'name') {
        text = value.toLowerCase();
        filteredPool = playerPool.filter(
            (player: any) => player.name.toLowerCase().includes(text.toLowerCase())
        );
    } else {
        filteredPool = playerPool.filter(
            (player: any) => player[attribute].includes(value)
        );
    }
    return {
        searchText: text,
        filteredPool: filteredPool
    }
};