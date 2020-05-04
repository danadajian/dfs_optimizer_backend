import {handleContestChange} from "./handleContestChange";
import {getPlayerPool} from "../../helpers/getPlayerPool/getPlayerPool";
import {createEmptyLineup} from "../../helpers/createEmptyLineup/createEmptyLineup";

jest.mock('../../helpers/getPlayerPool/getPlayerPool');
jest.mock('../../helpers/createEmptyLineup/createEmptyLineup');

(getPlayerPool as jest.Mock).mockReturnValue('player pool');
(createEmptyLineup as jest.Mock).mockReturnValue('empty lineup');

jest.spyOn(window, 'alert').mockImplementation(() => jest.fn());

const setState = jest.fn();

describe('handleContestChange', () => {
    describe('all data exists case', () => {
        let result: any;
        const state = {
            site: 'Fanduel',
            sport: 'mlb',
            dfsData: [
                {
                    contest: 'a contest',
                    players: 'dfsPlayers'
                },
                {
                    something: 'else'
                }
            ],
            projectionsData: {
                some: 'data'
            },
            opponentRanks: {},
            injuries: {},
            playerStatuses: []
        };
        const contest = 'a contest'
        const lineupPositions = ['P', 'C,1B', '2B', '3B', 'SS', 'OF', 'OF', 'OF', 'C,1B,2B,3B,SS,OF'];
        const displayMatrix = ['P', 'C/1B', '2B', '3B', 'SS', 'OF', 'OF', 'OF', 'Util'];
        beforeEach(async () => {
            // @ts-ignore
            result = await handleContestChange(contest, state, setState)
        })

        it('should call getPlayerPool with correct params', () => {
            expect(getPlayerPool).toHaveBeenCalledWith('dfsPlayers', state.projectionsData, state.site,
                state.opponentRanks, state.injuries, state.playerStatuses)
        });

        it('should call createEmptyLineup with correct params', () => {
            expect(createEmptyLineup).toHaveBeenCalledWith(lineupPositions, displayMatrix)
        });

        it('should call setState with correct params', () => {
            expect(setState).toHaveBeenCalledWith({
                site: 'Fanduel',
                sport: 'mlb',
                dfsData: [
                    {
                        contest: 'a contest',
                        players: 'dfsPlayers'
                    },
                    {
                        something: 'else'
                    }
                ],
                projectionsData: {
                    some: 'data'
                },
                opponentRanks: {},
                injuries: {},
                playerStatuses: [],
                contest,
                playerPool: 'player pool',
                filteredPool: [],
                whiteList: [],
                blackList: [],
                lineup: 'empty lineup',
                lineupPositions,
                displayMatrix,
                salaryCap: 35000,
                lineupRestrictions: {
                    'distinctTeamsRequired': 3,
                    'maxPlayersPerTeam': 4,
                    'teamAgnosticPosition': 'P'
                }
            })
        });
    });

    describe('no dfs data exists', () => {
        let result: any;
        const state = {
            site: 'Fanduel',
            sport: 'mlb',
            dfsData: [],
            projectionsData: {
                some: 'data'
            },
            opponentRanks: {},
            injuries: {},
            playerStatuses: []
        };
        const contest = 'a contest'
        beforeEach(async () => {
            // @ts-ignore
            result = await handleContestChange(contest, state, setState)
        })

        it('should call window alert', () => {
            expect(window.alert).toHaveBeenCalledWith('Fanduel data is currently unavailable.');
        });

        it('should return expected result', () => {
            expect(result).toBe(undefined)
        });
    });

    describe('no projection data exists', () => {
        let result: any;
        const state = {
            site: 'Fanduel',
            sport: 'mlb',
            dfsData: [
                {
                    some: 'stuff'
                }
            ],
            projectionsData: {},
            opponentRanks: {},
            injuries: {},
            playerStatuses: []
        };
        const contest = 'a contest'
        beforeEach(async () => {
            // @ts-ignore
            result = await handleContestChange(contest, state, setState)
        })

        it('should call window alert', () => {
            expect(window.alert).toHaveBeenCalledWith('Projection data is currently unavailable.');
        });

        it('should return expected result', () => {
            expect(result).toBe(undefined)
        });
    });

    afterEach(() => {
        jest.clearAllMocks()
    })
});