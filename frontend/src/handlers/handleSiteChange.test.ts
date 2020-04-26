import {handleSiteChange} from "./handleSiteChange";
import {INITIAL_STATE} from "../constants";

const setState = jest.fn();

describe('handleSiteChange', () => {
    let result: any;
    const site = 'a site'
    beforeEach(() => {
        result = handleSiteChange(site, setState)
    })

    it('should call setState with correct params', () => {
        expect(setState).toHaveBeenCalledWith({
            ...INITIAL_STATE,
            site,
        })
    });
});