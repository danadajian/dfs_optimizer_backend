import {extractContestsFromData} from './extractContestsFromData'

describe('extract contests from data', () => {
    const dataArray: any = [
        {
            contest: 'contest1 (4/20)'
        },
        {
            contest: 'contest2'
        }
    ];
    const date = new Date('4/20/2020');

    it('should return expected result for fd', () => {
        expect(extractContestsFromData(dataArray, 'fd', date)).toEqual(['contest1 (4/20)', 'contest2'])
    });

    it('should return expected result for dk', () => {
        expect(extractContestsFromData(dataArray, 'dk', date)).toEqual(['contest1 (4/20)'])
    });

});