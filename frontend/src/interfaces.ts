export interface State {
    isLoading: boolean,
    isOptimizing: boolean,
    date: Date,
    maxCombinations: number,
    salaryCap: number,
    whiteList: number[],
    blackList: number[],
    site: string,
    sport: string,
    contest: string,
    loadingText: string,
    searchText: string,
    sortValue: string,
    playerPool: playerPoolAttributes[],
    filteredPool: playerPoolAttributes[],
    lineup: lineupAttributes[],
    lineupPositions: string[],
    displayMatrix: string[],
    dfsData: any[],
    injuries: any,
    lineupRestrictions: any,
    opponentRanks: any,
    projectionsData: any,
    contests: any[],
    playerStatuses: any[],
}

export interface StateProps {
    state: State,
    setState: (state: State) => void
}

export interface lineupAttributes {
    lineupIndex: number,
    playerId: number,
    displayPosition: string,
    position?: string,
    team?: string,
    name?: string,
    status?: string,
    opponent?: string,
    gameDate?: string,
    projection?: number,
    salary?: number,
    opponentRank?: number,
    spread?: string,
    overUnder?: number
}

export interface lineupPlayerProps {
    player: lineupAttributes,
    onRemove: () => void,
    whiteList: number[],
    site: string
}

export interface blackListPlayerProps {
    player: lineupAttributes
}

export interface playerPoolAttributes {
    playerId: number,
    position: string,
    displayPosition: string,
    team: string,
    name: string,
    status: string,
    opponent: string,
    gameDate: string,
    projection: number,
    salary: number,
    opponentRank: number,
    spread: string,
    overUnder: number

    [key: string]: any
}
