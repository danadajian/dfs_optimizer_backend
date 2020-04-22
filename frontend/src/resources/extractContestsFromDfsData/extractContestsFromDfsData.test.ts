import {extractContestsFromDfsData} from './extractContestsFromDfsData'

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
        expect(extractContestsFromDfsData(dataArray, 'Fanduel', date)).toEqual(['contest1 (4/20)', 'contest2'])
    });

    it('should return expected result for dk', () => {
        expect(extractContestsFromDfsData(dataArray, 'DraftKings', date)).toEqual(['contest1 (4/20)'])
    });

    it('should return expected result for empty dataArray', () => {
        expect(extractContestsFromDfsData([], 'DraftKings', date)).toEqual([])
    });

});