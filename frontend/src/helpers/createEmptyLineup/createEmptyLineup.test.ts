import { createEmptyLineup } from './createEmptyLineup'

describe('gets empty lineup from matrix', () => {
    it('gets empty lineup', () => {
        let lineupPositions = ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'RB,WR,TE', 'D/ST'];
        let displayMatrix = ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'FLEX', 'D/ST'];
        let expectedJson = [
            {
                lineupIndex: 0,
                playerId: 0,
                position: 'QB',
                displayPosition: 'QB'
            },
            {
                lineupIndex: 1,
                playerId: 0,
                position: 'RB',
                displayPosition: 'RB'
            },
            {
                lineupIndex: 2,
                playerId: 0,
                position: 'RB',
                displayPosition: 'RB'
            },
            {
                lineupIndex: 3,
                playerId: 0,
                position: 'WR',
                displayPosition: 'WR'
            },
            {
                lineupIndex: 4,
                playerId: 0,
                position: 'WR',
                displayPosition: 'WR'
            },
            {
                lineupIndex: 5,
                playerId: 0,
                position: 'WR',
                displayPosition: 'WR'
            },
            {
                lineupIndex: 6,
                playerId: 0,
                position: 'TE',
                displayPosition: 'TE'
            },
            {
                lineupIndex: 7,
                playerId: 0,
                position: 'RB,WR,TE',
                displayPosition: 'FLEX'
            },
            {
                lineupIndex: 8,
                playerId: 0,
                position: 'D/ST',
                displayPosition: 'D/ST'
            }
        ];
        expect(createEmptyLineup(lineupPositions, displayMatrix)).toEqual(expectedJson);
    });
});