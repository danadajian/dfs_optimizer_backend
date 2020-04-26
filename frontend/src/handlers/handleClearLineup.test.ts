import {handleClearLineup} from "./handleClearLineup";
import {createEmptyLineup} from "../helpers/createEmptyLineup/createEmptyLineup";

jest.mock('../helpers/createEmptyLineup/createEmptyLineup');

(createEmptyLineup as jest.Mock).mockReturnValue('empty lineup');

const setState = jest.fn();

describe('handleClearLineup', () => {
    let result: any;
    const state = {
        some: 'state',
        idk: 'test',
        lineupPositions: 'lineupPositions',
        displayMatrix: 'displayMatrix'
    };
    beforeEach(() => {
        // @ts-ignore
        result = handleClearLineup(state, setState)
    })

    it('should call createEmptyLineup with correct params', () => {
        expect(createEmptyLineup).toHaveBeenCalledWith('lineupPositions', 'displayMatrix')
    });

    it('should call setState with correct params', () => {
        expect(setState).toHaveBeenCalledWith({
            some: 'state',
            idk: 'test',
            lineupPositions: 'lineupPositions',
            displayMatrix: 'displayMatrix',
            // @ts-ignore
            lineup: 'empty lineup',
            whiteList: [],
            blackList: []
        })
    });
});