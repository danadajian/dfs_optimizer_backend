export function getFilterPlayersState(attribute, value, state) {
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
            (player) => player.name.toLowerCase().includes(text.toLowerCase())
        );
    } else {
        filteredPool = playerPool.filter(
            (player) => player[attribute].includes(value)
        );
    }
    return {
        searchText: text,
        filteredPool: filteredPool
    }
}