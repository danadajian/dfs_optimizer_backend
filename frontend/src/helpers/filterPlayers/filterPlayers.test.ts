import {filterPlayers} from './filterPlayers'

describe('filters by attribute', () => {
    it('filters by name attribute', () => {
        let playerPool = [
            {name: 'Player0', playerId: 0, position: 'QB'},
            {name: 'Player1', playerId: 1, position: 'QB'},
            {name: 'Player2', playerId: 2, position: 'RB'},
            {name: 'Player3', playerId: 3, position: 'RB'},
            {name: 'Player4', playerId: 4, position: 'RB'},
            {name: 'Player5', playerId: 5, position: 'RB'}
        ];
        expect(filterPlayers('name', '4', playerPool)).toMatchObject({
            searchText: '4',
            filteredPool: [
                {name: 'Player4', playerId: 4, position: 'RB'}
            ]
        });
    });
    it('filters by non-name attribute', () => {
        let playerPool = [
            {name: 'Player0', playerId: 0, position: 'QB'},
            {name: 'Player1', playerId: 1, position: 'QB'},
            {name: 'Player2', playerId: 2, position: 'RB'},
            {name: 'Player3', playerId: 3, position: 'RB'},
            {name: 'Player4', playerId: 4, position: 'RB'},
            {name: 'Player5', playerId: 5, position: 'RB'}
        ];
        expect(filterPlayers('position', 'RB', playerPool)).toMatchObject({
            searchText: '',
            filteredPool: [
                {name: 'Player2', playerId: 2, position: 'RB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player4', playerId: 4, position: 'RB'},
                {name: 'Player5', playerId: 5, position: 'RB'}
            ]
        });
    });
    it('filters by position with multi-position player', () => {
        let playerPool = [
            {name: 'Player0', playerId: 0, position: 'QB'},
            {name: 'Player1', playerId: 1, position: 'QB'},
            {name: 'Player2', playerId: 2, position: 'RB'},
            {name: 'Player3', playerId: 3, position: 'RB/WR'},
            {name: 'Player4', playerId: 4, position: 'RB'},
            {name: 'Player5', playerId: 5, position: 'RB'}
        ];
        expect(filterPlayers('position', 'WR', playerPool)).toMatchObject({
            searchText: '',
            filteredPool: [
                {name: 'Player3', playerId: 3, position: 'RB/WR'}
            ]
        });
    });
    it('filters by all attributes', () => {
        let playerPool = [
            {name: 'Player0', playerId: 0, position: 'QB'},
            {name: 'Player1', playerId: 1, position: 'QB'},
            {name: 'Player2', playerId: 2, position: 'RB'},
            {name: 'Player3', playerId: 3, position: 'RB'},
            {name: 'Player4', playerId: 4, position: 'RB'},
            {name: 'Player5', playerId: 5, position: 'RB'}
        ];
        expect(filterPlayers('position', 'All', playerPool)).toMatchObject({
            searchText: '',
            filteredPool: [
                {name: 'Player0', playerId: 0, position: 'QB'},
                {name: 'Player1', playerId: 1, position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'RB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player4', playerId: 4, position: 'RB'},
                {name: 'Player5', playerId: 5, position: 'RB'}
            ]
        });
    });
});