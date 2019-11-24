import { createEmptyLineup } from '../functions/createEmptyLineup'

describe('gets empty lineup from matrix', () => {
    test('gets empty lineup', () => {
        let lineupMatrix = ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'RB,WR,TE', 'D/ST'];
        let displayMatrix = ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'FLEX', 'D/ST'];
        let expectedJson = [
            {
                'id': '',
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
                'id': '',
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
                'id': '',
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
                'id': '',
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
                'id': '',
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
                'id': '',
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
                'id': '',
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
                'id': '',
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
                'id': '',
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
        expect(createEmptyLineup(lineupMatrix, displayMatrix)).toEqual(expectedJson);
    });
});