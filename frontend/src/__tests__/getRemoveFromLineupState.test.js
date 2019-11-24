import { getRemoveFromLineupState } from '../functions/getRemoveFromLineupState'

describe('removes player to lineup', () => {
    test('can remove player', () => {
        let lineup = [
            {position: 'QB', team: '', name: 'Player0', playerId: 0, status: '', projected: '', salary: '', opponent: ''},
            {position: 'RB', team: '', name: 'Player1', playerId: 1, status: '', projected: '', salary: '', opponent: ''},
            {position: 'RB', team: '', name: '', playerId: '', status: '', projected: '', salary: '', opponent: ''},
            {position: 'RB,WR,TE', team: '', name: '', playerId: '', status: '', projected: '', salary: '', opponent: ''},
        ];
        let whiteList = [{position: 'RB', team: '', name: 'Player1', playerId: 1, status: '', projected: '', salary: '', opponent: ''}];
        let state = {lineup, whiteList};
        expect(getRemoveFromLineupState(1, state)).toMatchObject({
            lineup: [
                {position: 'QB', team: '', name: 'Player0', playerId: 0, status: '', projected: '', salary: '', opponent: ''},
                {position: 'RB', team: '', name: '', playerId: '', status: '', projected: '', salary: '', opponent: ''},
                {position: 'RB', team: '', name: '', playerId: '', status: '', projected: '', salary: '', opponent: ''},
                {position: 'RB,WR,TE', team: '', name: '', playerId: '', status: '', projected: '', salary: '', opponent: ''},
            ],
            whiteList: []
        });
    });
});