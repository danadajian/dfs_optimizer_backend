import {handleFilterPlayers} from './handleFilterPlayers'

const setState = jest.fn();

describe('filterPlayers', () => {
    describe('filters by name attribute', () => {
        let result: any;
        const state = {
            playerPool: [
                {name: 'Player1', playerId: 1, position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'QB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player4', playerId: 4, position: 'RB'},
                {name: 'Player5', playerId: 5, position: 'RB'},
                {name: 'Player6', playerId: 6, position: 'RB'}
            ]
        }

        beforeEach(() => {
            // @ts-ignore
            result = handleFilterPlayers('name', '4', state, setState)
        })

        it('should call setState with correct params', () => {
            expect(setState).toHaveBeenCalledWith({
                ...state,
                searchText: '4',
                filteredPool: [
                    {name: 'Player4', playerId: 4, position: 'RB'}
                ]
            })
        });
    })

    describe('filters by non-name attribute', () => {
        let result: any;
        const state = {
            playerPool: [
                {name: 'Player1', playerId: 1, position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'QB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player4', playerId: 4, position: 'RB'},
                {name: 'Player5', playerId: 5, position: 'RB'},
                {name: 'Player6', playerId: 6, position: 'RB'}
            ]
        }

        beforeEach(() => {
            // @ts-ignore
            result = handleFilterPlayers('position', 'RB', state, setState)
        })

        it('should call setState with correct params', () => {
            expect(setState).toHaveBeenCalledWith({
                ...state,
                searchText: '',
                filteredPool: [
                    {name: 'Player3', playerId: 3, position: 'RB'},
                    {name: 'Player4', playerId: 4, position: 'RB'},
                    {name: 'Player5', playerId: 5, position: 'RB'},
                    {name: 'Player6', playerId: 6, position: 'RB'}
                ]
            })
        });
    })

    describe('filters by position with multi-position player', () => {
        let result: any;
        const state = {
            playerPool: [
                {name: 'Player1', playerId: 1, position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'QB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player4', playerId: 4, position: 'RB/WR'},
                {name: 'Player5', playerId: 5, position: 'RB'},
                {name: 'Player6', playerId: 6, position: 'RB'}
            ]
        }

        beforeEach(() => {
            // @ts-ignore
            result = handleFilterPlayers('position', 'WR', state, setState)
        })

        it('should call setState with correct params', () => {
            expect(setState).toHaveBeenCalledWith({
                ...state,
                searchText: '',
                filteredPool: [
                    {name: 'Player4', playerId: 4, position: 'RB/WR'}
                ]
            })
        });
    })

    describe('filters by all attributes', () => {
        let result: any;
        const state = {
            playerPool: [
                {name: 'Player1', playerId: 1, position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'QB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player4', playerId: 4, position: 'RB'},
                {name: 'Player5', playerId: 5, position: 'RB'},
                {name: 'Player6', playerId: 6, position: 'RB'}
            ]
        }

        beforeEach(() => {
            // @ts-ignore
            result = handleFilterPlayers('position', 'All', state, setState)
        })

        it('should call setState with correct params', () => {
            expect(setState).toHaveBeenCalledWith({
                ...state,
                searchText: '',
                filteredPool: [
                    {name: 'Player1', playerId: 1, position: 'QB'},
                    {name: 'Player2', playerId: 2, position: 'QB'},
                    {name: 'Player3', playerId: 3, position: 'RB'},
                    {name: 'Player4', playerId: 4, position: 'RB'},
                    {name: 'Player5', playerId: 5, position: 'RB'},
                    {name: 'Player6', playerId: 6, position: 'RB'}
                ]
            })
        });
    })
});