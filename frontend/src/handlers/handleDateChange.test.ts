import {handleClearLineup} from "./handleClearLineup";
import {handleDateChange} from "./handleDateChange";

const setState = jest.fn();

describe('handleClearLineup', () => {
    let result: any;
    const state = {
        some: 'state',
        idk: 'test'
    };
    const date = 'a date'
    beforeEach(() => {
        // @ts-ignore
        result = handleDateChange(date, state, setState)
    })

    it('should call setState with correct params', () => {
        expect(setState).toHaveBeenCalledWith({
            some: 'state',
            idk: 'test',
            date
        })
    });
});