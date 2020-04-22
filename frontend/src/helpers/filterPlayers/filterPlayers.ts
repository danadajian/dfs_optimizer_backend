export const filterPlayers = (attribute: string, value: string, playerPool: any[]) => {
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
    return {
        searchText,
        filteredPool
    }
};