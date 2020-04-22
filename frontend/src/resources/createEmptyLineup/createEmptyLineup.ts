export const createEmptyLineup = (lineupPositions: string[], displayMatrix: any) => {
    return lineupPositions.map((position: string, index: number) => ({
        'playerId': 0,
        'position': position,
        'displayPosition': displayMatrix[index]
    }))
};