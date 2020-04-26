import {handleGenerateOptimalLineup} from "./handleGenerateOptimalLineup";
import {invokeLambdaFunction} from "../aws/aws";

jest.mock('../aws/aws');

jest.spyOn(window, 'alert').mockImplementation(() => jest.fn());

const setState = jest.fn();

describe('handleGenerateOptimalLineup', () => {
    describe('optimal lineup exists case', () => {
        let result: any;
        const state = {
            some: 'stuff',
            playerPool: [
                {
                    some: 'info',
                    playerId: 2
                },
                {
                    some: 'info',
                    playerId: 1
                },
                {
                    some: 'info',
                    playerId: 3
                }
            ],
            displayMatrix: ['display1', 'display2', 'display3']
        };
        beforeEach(async () => {
            (invokeLambdaFunction as jest.Mock).mockReturnValue([1, 2, 3]);
            // @ts-ignore
            result = await handleGenerateOptimalLineup(state, setState)
        })

        it('should initially call setState with correct params', () => {
            expect(setState).toHaveBeenNthCalledWith(1, {
                ...state,
                isOptimizing: true
            })
        });

        it('should call invokeLambdaFunction with correct params', () => {
            expect(invokeLambdaFunction).toHaveBeenCalledWith(process.env.REACT_APP_OPTIMAL_LINEUP_LAMBDA, state)
        });

        it('should call setState with correct params at the end', () => {
            expect(setState).toHaveBeenLastCalledWith({
                ...state,
                isOptimizing: false,
                lineup: [
                    {
                        some: 'info',
                        playerId: 1,
                        displayPosition: 'display1'
                    },
                    {
                        some: 'info',
                        playerId: 2,
                        displayPosition: 'display2'
                    },
                    {
                        some: 'info',
                        playerId: 3,
                        displayPosition: 'display3'
                    }
                ]
            })
        });
    });

    describe('optimal lineup error case', () => {
        let result: any;
        const state = {
            some: 'stuff'
        };
        beforeEach(async () => {
            (invokeLambdaFunction as jest.Mock).mockReturnValue({
                errorMessage: 'an error happened',
                errorType: 'a bad error',
                stackTrace: ['line1', 'line2', 'line3', 'line4', 'line5', 'line6', 'line7', 'line8']
            });
            // @ts-ignore
            result = await handleGenerateOptimalLineup(state, setState)
        })

        it('should initially call setState with correct params', () => {
            expect(setState).toHaveBeenNthCalledWith(1, {
                ...state,
                isOptimizing: true
            })
        });

        it('should call invokeLambdaFunction with correct params', () => {
            expect(invokeLambdaFunction).toHaveBeenCalledWith(process.env.REACT_APP_OPTIMAL_LINEUP_LAMBDA, state)
        });

        it('should call window alert', () => {
            expect(window.alert).toHaveBeenCalledWith('An error occurred.\na bad error\nan error happened\nline1,line2,line3,line4,line5,line6,line7')
        });
    });

    describe('optimal lineup empty case', () => {
        let result: any;
        const state = {
            some: 'stuff'
        };
        beforeEach(async () => {
            (invokeLambdaFunction as jest.Mock).mockReturnValue([]);
            // @ts-ignore
            result = await handleGenerateOptimalLineup(state, setState)
        })

        it('should initially call setState with correct params', () => {
            expect(setState).toHaveBeenNthCalledWith(1, {
                ...state,
                isOptimizing: true
            })
        });

        it('should call invokeLambdaFunction with correct params', () => {
            expect(invokeLambdaFunction).toHaveBeenCalledWith(process.env.REACT_APP_OPTIMAL_LINEUP_LAMBDA, state)
        });

        it('should call window alert', () => {
            expect(window.alert).toHaveBeenCalledWith('Failed to generate optimal lineup.')
        });
    });

    afterEach(() => {
        jest.clearAllMocks()
    })
});