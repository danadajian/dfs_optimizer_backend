import {handleAddPlayerToBlackList} from './handleAddPlayerToBlackList'

const setState = jest.fn();

describe('addPlayerToBlackList', () => {
    describe('add player to blacklist case', () => {
        let result: any;
        const state = {
            playerPool: [
                {name: 'Player0', playerId: 0, position: 'QB'},
                {name: 'Player1', playerId: 1, position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'RB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player4', playerId: 4, position: 'RB'},
                {name: 'Player5', playerId: 5, position: 'RB'}
            ],
            whiteList: [5],
            blackList: [0, 2, 3, 4],
            lineup: [
                {playerId: 0, position: 'QB', displayPosition: 'QB'},
                {name: 'Player5', playerId: 5, position: 'RB', displayPosition: 'RB'},
                {playerId: 0, position: 'RB', displayPosition: 'RB'},
                {playerId: 0, position: 'RB,WR,TE', displayPosition: 'FLEX'}
            ],
            lineupPositions: ['QB', 'RB', 'RB', 'RB,WR,TE'],
            displayMatrix: ['QB', 'RB', 'RB', 'FLEX']
        }
        beforeEach(() => {
            // @ts-ignore
            result = handleAddPlayerToBlackList(5, state, setState)
        })

        it('should call setState with correct params', () => {
            expect(setState).toHaveBeenCalledWith({
                ...state,
                lineup: [
                    {playerId: 0, position: 'QB', displayPosition: 'QB'},
                    {playerId: 0, position: 'RB', displayPosition: 'RB'},
                    {playerId: 0, position: 'RB', displayPosition: 'RB'},
                    {playerId: 0, position: 'RB,WR,TE', displayPosition: 'FLEX'}
                ],
                whiteList: [],
                blackList: [0, 2, 3, 4, 5],
                searchText: '',
                filteredPool: []
            })
        });
    })

    describe('removes player from blacklist case', () => {
        let result: any;
        const state = {
            playerPool: [
                {name: 'Player0', playerId: 0, position: 'QB'},
                {name: 'Player1', playerId: 1, position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'RB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player4', playerId: 4, position: 'RB'},
                {name: 'Player5', playerId: 5, position: 'RB'}
            ],
            whiteList: [],
            blackList: [0, 2, 3, 4, 5],
            lineup: [
                {playerId: 0, position: 'QB', displayPosition: 'QB'},
                {playerId: 0, position: 'RB', displayPosition: 'RB'},
                {playerId: 0, position: 'RB', displayPosition: 'RB'},
                {playerId: 0, position: 'RB,WR,TE', displayPosition: 'FLEX'}
            ],
            lineupPositions: ['QB', 'RB', 'RB', 'RB,WR,TE'],
            displayMatrix: ['QB', 'RB', 'RB', 'FLEX']
        }
        beforeEach(() => {
            // @ts-ignore
            result = handleAddPlayerToBlackList(5, state, setState)
        })

        it('should call setState with correct params', () => {
            expect(setState).toHaveBeenCalledWith({
                ...state,
                lineup: [
                    {playerId: 0, position: 'QB', displayPosition: 'QB'},
                    {playerId: 0, position: 'RB', displayPosition: 'RB'},
                    {playerId: 0, position: 'RB', displayPosition: 'RB'},
                    {playerId: 0, position: 'RB,WR,TE', displayPosition: 'FLEX'}
                ],
                whiteList: [],
                blackList: [0, 2, 3, 4],
                searchText: '',
                filteredPool: []
            })
        });
    })
});