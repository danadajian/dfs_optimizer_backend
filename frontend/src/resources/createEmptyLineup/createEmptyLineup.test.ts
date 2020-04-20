import { createEmptyLineup } from './createEmptyLineup'

describe('gets empty lineup from matrix', () => {
    it('gets empty lineup', () => {
        let lineupPositions = ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'RB,WR,TE', 'D/ST'];
        let displayMatrix = ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'FLEX', 'D/ST'];
        let expectedJson = [
            {
                'playerId': 0,
                'position': 'QB',
                'displayPosition': 'QB',
                'team': '',
                'name': '',
                'projection': '',
                'salary': '',
                'opponent': '',
                'gameDate': ''
            },
            {
                'playerId': 0,
                'position': 'RB',
                'displayPosition': 'RB',
                'team': '',
                'name': '',
                'projection': '',
                'salary': '',
                'opponent': '',
                'gameDate': ''
            },
            {
                'playerId': 0,
                'position': 'RB',
                'displayPosition': 'RB',
                'team': '',
                'name': '',
                'projection': '',
                'salary': '',
                'opponent': '',
                'gameDate': ''
            },
            {
                'playerId': 0,
                'position': 'WR',
                'displayPosition': 'WR',
                'team': '',
                'name': '',
                'projection': '',
                'salary': '',
                'opponent': '',
                'gameDate': ''
            },
            {
                'playerId': 0,
                'position': 'WR',
                'displayPosition': 'WR',
                'team': '',
                'name': '',
                'projection': '',
                'salary': '',
                'opponent': '',
                'gameDate': ''
            },
            {
                'playerId': 0,
                'position': 'WR',
                'displayPosition': 'WR',
                'team': '',
                'name': '',
                'projection': '',
                'salary': '',
                'opponent': '',
                'gameDate': ''
            },
            {
                'playerId': 0,
                'position': 'TE',
                'displayPosition': 'TE',
                'team': '',
                'name': '',
                'projection': '',
                'salary': '',
                'opponent': '',
                'gameDate': ''
            },
            {
                'playerId': 0,
                'position': 'RB,WR,TE',
                'displayPosition': 'FLEX',
                'team': '',
                'name': '',
                'projection': '',
                'salary': '',
                'opponent': '',
                'gameDate': ''
            },
            {
                'playerId': 0,
                'position': 'D/ST',
                'displayPosition': 'D/ST',
                'team': '',
                'name': '',
                'projection': '',
                'salary': '',
                'opponent': '',
                'gameDate': ''
            }
        ];
        expect(createEmptyLineup(lineupPositions, displayMatrix)).toEqual(expectedJson);
    });
});