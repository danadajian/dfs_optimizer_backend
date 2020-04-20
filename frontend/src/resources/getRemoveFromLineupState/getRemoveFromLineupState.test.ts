import { getRemoveFromLineupState } from './getRemoveFromLineupState'

describe('removes player to lineup', () => {
    it('can remove player', () => {
        let lineup = [
            {position: 'QB', displayPosition: 'QB', team: '', name: 'Player1', playerId: 1, projected: '', salary: '', opponent: ''},
            {position: 'RB', displayPosition: 'RB', team: '', name: 'Player2', playerId: 2, projected: '', salary: '', opponent: ''},
            {position: 'RB', displayPosition: 'RB', team: '', name: '', playerId: 0, projected: '', salary: '', opponent: ''},
            {position: 'RB,WR,TE', displayPosition: 'FLEX', team: '', name: '', playerId: 0, projected: '', salary: '', opponent: ''},
        ];
        let whiteList = [2];
        let lineupPositions = ['QB', 'RB', 'RB', 'RB,WR,TE'];
        let displayMatrix = ['QB', 'RB', 'RB', 'FLEX'];
        let state = {lineup, whiteList, lineupPositions, displayMatrix};
        expect(getRemoveFromLineupState(1, state)).toMatchObject({
            lineup: [
                {position: 'QB', displayPosition: 'QB', team: '', name: 'Player1', playerId: 1, projected: '', salary: '', opponent: ''},
                {position: 'RB', displayPosition: 'RB', team: '', name: '', playerId: 0, projected: '', salary: '', opponent: ''},
                {position: 'RB', displayPosition: 'RB', team: '', name: '', playerId: 0, projected: '', salary: '', opponent: ''},
                {position: 'RB,WR,TE', displayPosition: 'FLEX', team: '', name: '', playerId: 0, projected: '', salary: '', opponent: ''},
            ],
            whiteList: []
        });
    });
});