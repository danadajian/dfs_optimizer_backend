export interface lineupAttributes {
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
}