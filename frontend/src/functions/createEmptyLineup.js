export function createEmptyLineup(lineupPositions, displayMatrix) {
    return lineupPositions.map((position, index) => ({
        'playerId': 0,
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