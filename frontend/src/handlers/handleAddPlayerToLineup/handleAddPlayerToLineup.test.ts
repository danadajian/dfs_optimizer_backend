import {handleAddPlayerToLineup} from './handleAddPlayerToLineup'

jest.spyOn(window, 'alert').mockImplementation(() => jest.fn());

const setState = jest.fn();

describe('cannot add to lineup case', () => {
    const stateConstants = {
        playerPool: [
            {name: 'Player1', playerId: 1, position: 'QB'},
            {name: 'Player2', playerId: 2, position: 'QB'},
            {name: 'Player3', playerId: 3, position: 'RB'},
            {name: 'Player4', playerId: 4, position: 'RB'},
            {name: 'Player5', playerId: 5, position: 'RB'},
            {name: 'Player6', playerId: 6, position: 'RB'}
        ],
        filteredPool: [],
        whiteList: [],
        blackList: [1, 3, 4, 5]
    }
    describe('player already in lineup', () => {
        let result: any;
        const state = {
            ...stateConstants,
            lineup: [
                {lineupIndex: 0, name: 'Player1', playerId: 1, position: 'QB'},
                {lineupIndex: 1, playerId: 0, position: 'RB'},
                {lineupIndex: 2, playerId: 0, position: 'RB'},
                {lineupIndex: 3, playerId: 0, position: 'RB,WR,TE'}
            ]
        }

        beforeEach(() => {
            // @ts-ignore
            result = handleAddPlayerToLineup(0, state, setState)
        })

        it('should alert that the player was already added', () => {
            expect(window.alert).toHaveBeenLastCalledWith('Player already added to lineup.');
        });

        it('should not change the state', () => {
            expect(setState).not.toHaveBeenCalled();
        });
    })

    describe('not enough non-flex positions available', () => {
        let result: any;
        const state = {
            ...stateConstants,
            lineup: [
                {lineupIndex: 0, name: 'Player1', playerId: 1, position: 'QB'},
                {lineupIndex: 1, playerId: 0, position: 'RB'},
                {lineupIndex: 2, playerId: 0, position: 'RB'},
                {lineupIndex: 3, playerId: 0, position: 'RB,WR,TE'}
            ]
        }

        beforeEach(() => {
            // @ts-ignore
            result = handleAddPlayerToLineup(1, state, setState)
        })

        it('should alert that not enough positions are available', () => {
            expect(window.alert).toHaveBeenLastCalledWith('Not enough positions available to add player.');
        });

        it('should not change the state', () => {
            expect(setState).not.toHaveBeenCalled();
        });
    })

    describe('not enough flex positions available', () => {
        let result: any;
        const state = {
            ...stateConstants,
            lineup: [
                {lineupIndex: 0, playerId: 0, position: 'QB'},
                {lineupIndex: 1, name: 'Player3', playerId: 3, position: 'RB'},
                {lineupIndex: 2, name: 'Player4', playerId: 4, position: 'RB'},
                {lineupIndex: 3, name: 'Player5', playerId: 5, position: 'RB'}
            ]
        }

        beforeEach(() => {
            // @ts-ignore
            result = handleAddPlayerToLineup(5, state, setState)
        })

        it('should alert that not enough positions are available', () => {
            expect(window.alert).toHaveBeenLastCalledWith('Not enough positions available to add player.');
        });

        it('should not change the state', () => {
            expect(setState).not.toHaveBeenCalled();
        });
    })

})

describe('can add to lineup case', () => {

    describe('can add non-flex position', () => {
        let result: any;
        const state = {
            playerPool: [
                {name: 'Player1', playerId: 1, position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'QB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player4', playerId: 4, position: 'RB'},
                {name: 'Player5', playerId: 5, position: 'RB'},
                {name: 'Player6', playerId: 6, position: 'RB'}
            ],
            filteredPool: [],
            whiteList: [],
            blackList: [1, 3, 4, 5],
            lineup: [
                {lineupIndex: 0, playerId: 0, position: 'QB'},
                {lineupIndex: 1, playerId: 0, position: 'RB'},
                {lineupIndex: 2, playerId: 0, position: 'RB'},
                {lineupIndex: 3, playerId: 0, position: 'RB,WR,TE'}
            ]
        }

        beforeEach(() => {
            // @ts-ignore
            result = handleAddPlayerToLineup(0, state, setState)
        })

        it('should call setState with correct params', () => {
            expect(setState).toHaveBeenCalledWith({
                ...state,
                lineup: [
                    {lineupIndex: 0, name: 'Player1', playerId: 1, position: 'QB'},
                    {lineupIndex: 1, playerId: 0, position: 'RB'},
                    {lineupIndex: 2, playerId: 0, position: 'RB'},
                    {lineupIndex: 3, playerId: 0, position: 'RB,WR,TE'}
                ],
                whiteList: [1],
                blackList: [3, 4, 5],
                searchText: '',
                filteredPool: [],
                sortValue: 'All'
            })
        });
    })

    describe('can add flex position to non-flex spot', () => {
        let result: any;
        const state = {
            playerPool: [
                {name: 'Player1', playerId: 1, position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'QB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player4', playerId: 4, position: 'RB'},
                {name: 'Player5', playerId: 5, position: 'RB'},
                {name: 'Player6', playerId: 6, position: 'RB'}
            ],
            filteredPool: [],
            whiteList: [],
            blackList: [1, 3, 4, 5],
            lineup: [
                {lineupIndex: 0, playerId: 0, position: 'QB'},
                {lineupIndex: 1, name: 'Player3', playerId: 3, position: 'RB'},
                {lineupIndex: 2, playerId: 0, position: 'RB'},
                {lineupIndex: 3, playerId: 0, position: 'RB,WR,TE'}
            ]
        }

        beforeEach(() => {
            // @ts-ignore
            result = handleAddPlayerToLineup(3, state, setState)
        })

        it('should call setState with correct params', () => {
            expect(setState).toHaveBeenCalledWith({
                ...state,
                lineup: [
                    {lineupIndex: 0, playerId: 0, position: 'QB'},
                    {lineupIndex: 1, name: 'Player3', playerId: 3, position: 'RB'},
                    {lineupIndex: 2, name: 'Player4', playerId: 4, position: 'RB'},
                    {lineupIndex: 3, playerId: 0, position: 'RB,WR,TE'}
                ],
                whiteList: [4],
                blackList: [1, 3, 5],
                searchText: '',
                filteredPool: [],
                sortValue: 'All'
            })
        });
    })

    describe('can add flex position to flex spot', () => {
        let result: any;
        const state = {
            playerPool: [
                {name: 'Player1', playerId: 1, position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'QB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player4', playerId: 4, position: 'RB'},
                {name: 'Player5', playerId: 5, position: 'RB'},
                {name: 'Player6', playerId: 6, position: 'RB'}
            ],
            filteredPool: [],
            whiteList: [],
            blackList: [1, 3, 4, 5],
            lineup: [
                {lineupIndex: 0, playerId: 0, position: 'QB'},
                {lineupIndex: 1, name: 'Player3', playerId: 3, position: 'RB'},
                {lineupIndex: 2, name: 'Player4', playerId: 4, position: 'RB'},
                {lineupIndex: 3, playerId: 0, position: 'RB,WR,TE'}
            ]
        }

        beforeEach(() => {
            // @ts-ignore
            result = handleAddPlayerToLineup(4, state, setState)
        })

        it('should call setState with correct params', () => {
            expect(setState).toHaveBeenCalledWith({
                ...state,
                lineup: [
                    {lineupIndex: 0, playerId: 0, position: 'QB'},
                    {lineupIndex: 1, name: 'Player3', playerId: 3, position: 'RB'},
                    {lineupIndex: 2, name: 'Player4', playerId: 4, position: 'RB'},
                    {lineupIndex: 3, name: 'Player5', playerId: 5, position: 'RB,WR,TE'}
                ],
                whiteList: [5],
                blackList: [1, 3, 4],
                searchText: '',
                filteredPool: [],
                sortValue: 'All'
            })
        });
    })

    describe('can add player to multi-position spot', () => {
        let result: any;
        const state = {
            playerPool: [
                {name: 'Player1', playerId: 1, position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'QB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player4', playerId: 4, position: 'RB'},
                {name: 'Player5', playerId: 5, position: 'RB'},
                {name: 'Player6', playerId: 6, position: 'WR'}
            ],
            filteredPool: [],
            whiteList: [],
            blackList: [],
            lineup: [
                {lineupIndex: 0, playerId: 0, position: 'QB'},
                {lineupIndex: 1, name: 'Player3', playerId: 3, position: 'RB'},
                {lineupIndex: 2, name: 'Player4', playerId: 4, position: 'RB'},
                {lineupIndex: 3, playerId: 0, position: 'RB/WR'}
            ]
        }

        beforeEach(() => {
            // @ts-ignore
            result = handleAddPlayerToLineup(5, state, setState)
        })

        it('should call setState with correct params', () => {
            expect(setState).toHaveBeenCalledWith({
                ...state,
                lineup: [
                    {lineupIndex: 0, playerId: 0, position: 'QB'},
                    {lineupIndex: 1, name: 'Player3', playerId: 3, position: 'RB'},
                    {lineupIndex: 2, name: 'Player4', playerId: 4, position: 'RB'},
                    {lineupIndex: 3, name: 'Player6', playerId: 6, position: 'RB/WR'}
                ],
                whiteList: [6],
                blackList: [],
                searchText: '',
                filteredPool: [],
                sortValue: 'All'
            })
        });
    })

    describe('can add multi-position player', () => {
        let result: any;
        const state = {
            playerPool: [
                {name: 'Player1', playerId: 1, position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'QB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player4', playerId: 4, position: 'RB'},
                {name: 'Player5', playerId: 5, position: 'RB'},
                {name: 'Player6', playerId: 6, position: 'RB/WR'}
            ],
            filteredPool: [],
            whiteList: [],
            blackList: [],
            lineup: [
                {lineupIndex: 0, playerId: 0, position: 'QB'},
                {lineupIndex: 1, name: 'Player3', playerId: 3, position: 'RB'},
                {lineupIndex: 2, name: 'Player4', playerId: 4, position: 'RB'},
                {lineupIndex: 3, playerId: 0, position: 'WR'}
            ]
        }

        beforeEach(() => {
            // @ts-ignore
            result = handleAddPlayerToLineup(5, state, setState)
        })

        it('should call setState with correct params', () => {
            expect(setState).toHaveBeenCalledWith({
                ...state,
                lineup: [
                    {lineupIndex: 0, playerId: 0, position: 'QB'},
                    {lineupIndex: 1, name: 'Player3', playerId: 3, position: 'RB'},
                    {lineupIndex: 2, name: 'Player4', playerId: 4, position: 'RB'},
                    {lineupIndex: 3, name: 'Player6', playerId: 6, position: 'WR'}
                ],
                whiteList: [6],
                blackList: [],
                searchText: '',
                filteredPool: [],
                sortValue: 'All'
            })
        });
    })
});
