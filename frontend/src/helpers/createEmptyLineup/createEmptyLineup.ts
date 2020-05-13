export const createEmptyLineup = (lineupPositions: string[], displayMatrix: any) => {
    return lineupPositions.map((position: string, lineupIndex: number) => ({
        lineupIndex,
        playerId: 0,
        position: position,
        displayPosition: displayMatrix[lineupIndex]
    }))
};