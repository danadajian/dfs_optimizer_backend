function createEmptyLineup(lineupMatrix, displayMatrix) {
    return lineupMatrix.map((position, index) => ({
        'playerId': '',
        'position': position,
        'displayPosition': displayMatrix[index],
        'team': '',
        'name': '',
        'projection': '',
        'salary': '',
        'opponent': '',
        'gameDate': ''
    }))
}

export { createEmptyLineup }