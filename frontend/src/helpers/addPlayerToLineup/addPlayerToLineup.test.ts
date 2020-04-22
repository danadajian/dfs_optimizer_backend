import { addPlayerToLineup } from './addPlayerToLineup'

jest.spyOn(window, 'alert').mockImplementation(() => jest.fn());

describe('adds player to lineup', () => {
    let playerPool = [
        {name: 'Player0', playerId: 0, position: 'QB'},
        {name: 'Player1', playerId: 1, position: 'QB'},
        {name: 'Player2', playerId: 2, position: 'RB'},
        {name: 'Player3', playerId: 3, position: 'RB'},
        {name: 'Player4', playerId: 4, position: 'RB'},
        {name: 'Player5', playerId: 5, position: 'RB'}
        ];
    let whiteList: any = [];
    let blackList = [0, 2, 3, 4];
    it('player already added', () => {
        let lineup = [
            {name: 'Player0', playerId: 0, position: 'QB'},
            {name: '', playerId: '', position: 'RB'},
            {name: '', playerId: '', position: 'RB'},
            {name: '', playerId: '', position: 'RB,WR,TE'}
        ];
        expect(addPlayerToLineup(0, playerPool, lineup, whiteList, blackList)).toEqual({});
        expect(window.alert).toHaveBeenLastCalledWith('Player already added to lineup.');
    });
    it('not enough non-flex positions available', () => {
        let lineup = [
            {name: 'Player0', playerId: 0, position: 'QB'},
            {name: '', playerId: '', position: 'RB'},
            {name: '', playerId: '', position: 'RB'},
            {name: '', playerId: '', position: 'RB,WR,TE'}
        ];
        expect(addPlayerToLineup(1, playerPool, lineup, whiteList, blackList)).toEqual({});
        expect(window.alert).toHaveBeenLastCalledWith('Not enough positions available to add player.');
    });
    it('not enough flex positions available', () => {
        let lineup = [
            {name: '', position: 'QB'},
            {name: 'Player2', playerId: 2, position: 'RB'},
            {name: 'Player3', playerId: 3, position: 'RB'},
            {name: 'Player4', playerId: 4, position: 'RB'}
        ];
        expect(addPlayerToLineup(5, playerPool, lineup, whiteList, blackList)).toEqual({});
        expect(window.alert).toHaveBeenLastCalledWith('Not enough positions available to add player.');
    });
    it('can add non-flex position', () => {
        let playerPool = [
        {name: 'Player0', playerId: 0, position: 'QB'},
        {name: 'Player1', playerId: 1, position: 'QB'},
        {name: 'Player2', playerId: 2, position: 'RB'},
        {name: 'Player3', playerId: 3, position: 'RB'},
        {name: 'Player4', playerId: 4, position: 'RB'},
        {name: 'Player5', playerId: 5, position: 'RB'}
        ];
        let whiteList: any = [];
        let blackList = [0, 2, 3, 4];
        let lineup = [
            {name: '', playerId: '', position: 'QB'},
            {name: '', playerId: '', position: 'RB'},
            {name: '', playerId: '', position: 'RB'},
            {name: '', playerId: '', position: 'RB,WR,TE'}
        ];
        expect(addPlayerToLineup(0, playerPool, lineup, whiteList, blackList)).toMatchObject({
            lineup: [
                {name: 'Player0', playerId: 0, position: 'QB'},
                {name: '', playerId: '', position: 'RB'},
                {name: '', playerId: '', position: 'RB'},
                {name: '', playerId: '', position: 'RB,WR,TE'}
            ],
            whiteList: [0],
            blackList: [2, 3, 4]
        });
    });
    it('can add flex position to non-flex spot', () => {
        let lineup = [
                {name: '', playerId: '', position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'RB'},
                {name: '', playerId: '', position: 'RB'},
                {name: '', playerId: '', position: 'RB,WR,TE'}
                ];
        let playerPool = [
            {name: 'Player0', playerId: 0, position: 'QB'},
            {name: 'Player1', playerId: 1, position: 'QB'},
            {name: 'Player2', playerId: 2, position: 'RB'},
            {name: 'Player3', playerId: 3, position: 'RB'},
            {name: 'Player4', playerId: 4, position: 'RB'},
            {name: 'Player5', playerId: 5, position: 'RB'}
            ];
        let whiteList: any = [];
        let blackList = [0, 2, 3, 4];
        expect(addPlayerToLineup(3, playerPool, lineup, whiteList, blackList)).toMatchObject({
            lineup: [
                {name: '', playerId: '', position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'RB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: '', playerId: '', position: 'RB,WR,TE'}
            ],
            whiteList: [3],
            blackList: [0, 2, 4]
        });
    });
    it('can add flex position to flex spot', () => {
        let lineup = [
            {name: '', playerId: '', position: 'QB'},
            {name: 'Player2', playerId: 2, position: 'RB'},
            {name: 'Player3', playerId: 3, position: 'RB'},
            {name: '', playerId: '', position: 'RB,WR,TE'}
        ];
        let playerPool = [
            {name: 'Player0', playerId: 0, position: 'QB'},
            {name: 'Player1', playerId: 1, position: 'QB'},
            {name: 'Player2', playerId: 2, position: 'RB'},
            {name: 'Player3', playerId: 3, position: 'RB'},
            {name: 'Player4', playerId: 4, position: 'RB'},
            {name: 'Player5', playerId: 5, position: 'RB'}
            ];
        let whiteList: any = [];
        let blackList = [0, 2, 3, 4];
        expect(playerPool[4]).toMatchObject({name: 'Player4', position: 'RB'});
        expect(addPlayerToLineup(4, playerPool, lineup, whiteList, blackList)).toMatchObject({
            lineup: [
                {name: '', playerId: '', position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'RB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player4', playerId: 4, position: 'RB,WR,TE'}
            ],
            whiteList: [4],
            blackList: [0, 2, 3]
        });
    });
    it('can add player to multi-position spot', () => {
        let lineup = [
            {name: '', playerId: '', position: 'QB'},
            {name: 'Player2', playerId: 2, position: 'RB'},
            {name: 'Player3', playerId: 3, position: 'RB'},
            {name: '', playerId: '', position: 'RB/WR'}
        ];
        let playerPool = [
            {name: 'Player0', playerId: 0, position: 'QB'},
            {name: 'Player1', playerId: 1, position: 'QB'},
            {name: 'Player2', playerId: 2, position: 'RB'},
            {name: 'Player3', playerId: 3, position: 'RB'},
            {name: 'Player4', playerId: 4, position: 'RB'},
            {name: 'Player5', playerId: 5, position: 'WR'}
            ];
        let whiteList: any = [];
        let blackList: any = [];
        expect(addPlayerToLineup(5, playerPool, lineup, whiteList, blackList)).toMatchObject({
            lineup: [
                {name: '', playerId: '', position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'RB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player5', playerId: 5, position: 'RB/WR'}
            ],
            whiteList: [5],
            blackList: []
        });
    });
    it('can add multi-position player', () => {
        let lineup = [
            {name: '', playerId: '', position: 'QB'},
            {name: 'Player2', playerId: 2, position: 'RB'},
            {name: 'Player3', playerId: 3, position: 'RB'},
            {name: '', playerId: '', position: 'WR'}
        ];
        let playerPool = [
            {name: 'Player0', playerId: 0, position: 'QB'},
            {name: 'Player1', playerId: 1, position: 'QB'},
            {name: 'Player2', playerId: 2, position: 'RB'},
            {name: 'Player3', playerId: 3, position: 'RB'},
            {name: 'Player4', playerId: 4, position: 'RB'},
            {name: 'Player5', playerId: 5, position: 'RB/WR'}
            ];
        let whiteList: any = [];
        let blackList: any = [];
        expect(addPlayerToLineup(5, playerPool, lineup, whiteList, blackList)).toMatchObject({
            lineup: [
                {name: '', playerId: '', position: 'QB'},
                {name: 'Player2', playerId: 2, position: 'RB'},
                {name: 'Player3', playerId: 3, position: 'RB'},
                {name: 'Player5', playerId: 5, position: 'WR'}
            ],
            whiteList: [5],
            blackList: []
        });
    });
});
