import { getFilterPlayersState } from '../functions/getFilterPlayersState'

describe('filters by attribute', () => {
    test('filters by name attribute', () => {
        let playerPool = [
        {name: 'Player0', playerId: 0, position: 'QB'},
        {name: 'Player1', playerId: 1, position: 'QB'},
        {name: 'Player2', playerId: 2, position: 'RB'},
        {name: 'Player3', playerId: 3, position: 'RB'},
        {name: 'Player4', playerId: 4, position: 'RB'},
        {name: 'Player5', playerId: 5, position: 'RB'}
        ];
        let text = 'test';
        let state = {playerPool, text};
        expect(getFilterPlayersState('name', '4', state)).toMatchObject({
            searchText: '4',
            filteredPool: [
            {name: 'Player4', playerId: 4, position: 'RB'}
            ]
        });
    });
    test('filters by non-name attribute', () => {
        let playerPool = [
        {name: 'Player0', playerId: 0, position: 'QB'},
        {name: 'Player1', playerId: 1, position: 'QB'},
        {name: 'Player2', playerId: 2, position: 'RB'},
        {name: 'Player3', playerId: 3, position: 'RB'},
        {name: 'Player4', playerId: 4, position: 'RB'},
        {name: 'Player5', playerId: 5, position: 'RB'}
        ];
        let text = 'test';
        let state = {playerPool, text};
        expect(getFilterPlayersState('position', 'RB', state)).toMatchObject({
            searchText: 'test',
            filteredPool: [
            {name: 'Player2', playerId: 2, position: 'RB'},
            {name: 'Player3', playerId: 3, position: 'RB'},
            {name: 'Player4', playerId: 4, position: 'RB'},
            {name: 'Player5', playerId: 5, position: 'RB'}
            ]
        });
    });
    test('filters by position with multi-position player', () => {
        let playerPool = [
        {name: 'Player0', playerId: 0, position: 'QB'},
        {name: 'Player1', playerId: 1, position: 'QB'},
        {name: 'Player2', playerId: 2, position: 'RB'},
        {name: 'Player3', playerId: 3, position: 'RB/WR'},
        {name: 'Player4', playerId: 4, position: 'RB'},
        {name: 'Player5', playerId: 5, position: 'RB'}
        ];
        let text = 'test';
        let state = {playerPool, text};
        expect(getFilterPlayersState('position', 'WR', state)).toMatchObject({
            searchText: 'test',
            filteredPool: [
            {name: 'Player3', playerId: 3, position: 'RB/WR'}
            ]
        });
    });
    test('filters by all attributes', () => {
        let playerPool = [
        {name: 'Player0', playerId: 0, position: 'QB'},
        {name: 'Player1', playerId: 1, position: 'QB'},
        {name: 'Player2', playerId: 2, position: 'RB'},
        {name: 'Player3', playerId: 3, position: 'RB'},
        {name: 'Player4', playerId: 4, position: 'RB'},
        {name: 'Player5', playerId: 5, position: 'RB'}
        ];
        let text = 'test';
        let state = {playerPool, text};
        expect(getFilterPlayersState('position', 'All', state)).toMatchObject({
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