import { getToggleBlackListState } from '../functions/getToggleBlackListState'

describe('can toggle blacklist', () => {
    test('adds player to blacklist', () => {
        let playerPool = [
        {name: 'Player0', playerId: 0, position: 'QB'},
        {name: 'Player1', playerId: 1, position: 'QB'},
        {name: 'Player2', playerId: 2, position: 'RB'},
        {name: 'Player3', playerId: 3, position: 'RB'},
        {name: 'Player4', playerId: 4, position: 'RB'},
        {name: 'Player5', playerId: 5, position: 'RB'}
        ];
        let whiteList = [5];
        let blackList = [0, 2, 3, 4];
        let lineup = [
            {name: '', playerId: '', position: 'QB'},
            {name: 'Player5', playerId: 5, position: 'RB'},
            {name: '', playerId: '', position: 'RB'},
            {name: '', playerId: '', position: 'RB,WR,TE', displayPosition: 'FLEX'}
        ];
        let lineupMatrix = ['QB', 'RB', 'RB', 'RB,WR,TE'];
        let displayMatrix = ['QB', 'RB', 'RB', 'FLEX'];
        let state = {playerPool, lineup, whiteList, blackList, lineupMatrix, displayMatrix};
        expect(getToggleBlackListState(5, state)).toMatchObject({
            lineup: [
                {name: '', playerId: '', position: 'QB'},
                {name: '', playerId: '', position: 'RB'},
                {name: '', playerId: '', position: 'RB'},
                {name: '', playerId: '', position: 'RB,WR,TE', displayPosition: 'FLEX'}
            ],
            whiteList: [],
            blackList: [0, 2, 3, 4, 5],
            filteredPool: null,
            searchText: ''
        });
    });
    test('removes player from blacklist', () => {
        let playerPool = [
        {name: 'Player0', playerId: 0, position: 'QB'},
        {name: 'Player1', playerId: 1, position: 'QB'},
        {name: 'Player2', playerId: 2, position: 'RB'},
        {name: 'Player3', playerId: 3, position: 'RB'},
        {name: 'Player4', playerId: 4, position: 'RB'},
        {name: 'Player5', playerId: 5, position: 'RB'}
        ];
        let whiteList = [];
        let blackList = [0, 2, 3, 4, 5];
        let lineup = [
            {name: '', playerId: '', position: 'QB'},
            {name: '', playerId: '', position: 'RB'},
            {name: '', playerId: '', position: 'RB'},
            {name: '', playerId: '', position: 'RB,WR,TE', displayPosition: 'FLEX'}
        ];
        let lineupMatrix = ['QB', 'RB', 'RB', 'RB,WR,TE'];
        let displayMatrix = ['QB', 'RB', 'RB', 'FLEX'];
        let state = {playerPool, lineup, whiteList, blackList, lineupMatrix, displayMatrix};
        expect(getToggleBlackListState(5, state)).toMatchObject({
            lineup: [
                {name: '', playerId: '', position: 'QB'},
                {name: '', playerId: '', position: 'RB'},
                {name: '', playerId: '', position: 'RB'},
                {name: '', playerId: '', position: 'RB,WR,TE', displayPosition: 'FLEX'}
            ],
            whiteList: [],
            blackList: [0, 2, 3, 4],
            filteredPool: null,
            searchText: ''
        });
    });
});