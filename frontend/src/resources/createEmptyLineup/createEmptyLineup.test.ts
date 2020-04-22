import { createEmptyLineup } from './createEmptyLineup'

describe('gets empty lineup from matrix', () => {
    it('gets empty lineup', () => {
        let lineupPositions = ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'RB,WR,TE', 'D/ST'];
        let displayMatrix = ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'FLEX', 'D/ST'];
        let expectedJson = [
            {
                'playerId': 0,
                'position': 'QB',
                'displayPosition': 'QB'
            },
            {
                'playerId': 0,
                'position': 'RB',
                'displayPosition': 'RB'
            },
            {
                'playerId': 0,
                'position': 'RB',
                'displayPosition': 'RB'
            },
            {
                'playerId': 0,
                'position': 'WR',
                'displayPosition': 'WR'
            },
            {
                'playerId': 0,
                'position': 'WR',
                'displayPosition': 'WR'
            },
            {
                'playerId': 0,
                'position': 'WR',
                'displayPosition': 'WR'
            },
            {
                'playerId': 0,
                'position': 'TE',
                'displayPosition': 'TE'
            },
            {
                'playerId': 0,
                'position': 'RB,WR,TE',
                'displayPosition': 'FLEX'
            },
            {
                'playerId': 0,
                'position': 'D/ST',
                'displayPosition': 'D/ST'
            }
        ];
        expect(createEmptyLineup(lineupPositions, displayMatrix)).toEqual(expectedJson);
    });
});