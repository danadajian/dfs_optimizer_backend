import { sumAttribute } from './sumAttribute'

describe('sums attributes correctly', () => {
    it('attributes included', () => {
        let lineup = [{'projected': 6.1}, {'projected': 7.2}, {'projected': 8.3}];
        expect(sumAttribute(lineup, 'projected')).toEqual(6.1 + 7.2 + 8.3);
    });

    it('attribute omitted', () => {
        let lineup = [{'projected': 6.1}, {'projected': ''}, {'projected': 8.3}];
        expect(sumAttribute(lineup, 'projected')).toEqual(6.1 + 8.3);
    });
});