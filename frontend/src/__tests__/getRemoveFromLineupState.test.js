import { getRemoveFromLineupState } from '../functions/getRemoveFromLineupState'

describe('removes player to lineup', () => {
    test('can remove player', () => {
        let lineup = [
            {position: 'QB', displayPosition: 'QB', team: '', name: 'Player0', playerId: 0, projected: '', salary: '', opponent: ''},
            {position: 'RB', displayPosition: 'RB', team: '', name: 'Player1', playerId: 1, projected: '', salary: '', opponent: ''},
            {position: 'RB', displayPosition: 'RB', team: '', name: '', playerId: '', projected: '', salary: '', opponent: ''},
            {position: 'RB,WR,TE', displayPosition: 'FLEX', team: '', name: '', playerId: '', projected: '', salary: '', opponent: ''},
        ];
        let whiteList = [{position: 'RB', team: '', name: 'Player1', playerId: 1, projected: '', salary: '', opponent: ''}];
        let state = {lineup, whiteList};
        expect(getRemoveFromLineupState(1, state)).toMatchObject({
            lineup: [
                {position: 'QB', displayPosition: 'QB', team: '', name: 'Player0', playerId: 0, projected: '', salary: '', opponent: ''},
                {position: 'RB', displayPosition: 'RB', team: '', name: '', playerId: '', projected: '', salary: '', opponent: ''},
                {position: 'RB', displayPosition: 'RB', team: '', name: '', playerId: '', projected: '', salary: '', opponent: ''},
                {position: 'RB,WR,TE', displayPosition: 'FLEX', team: '', name: '', playerId: '', projected: '', salary: '', opponent: ''},
            ],
            whiteList: []
        });
    });
});