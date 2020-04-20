import { formatDate } from './formatDate'

describe('gets correct date', () => {
    it('gets correct date', () => {
        let date = new Date(2019, 10, 22); // month is 1 less than expected
        expect(formatDate(date)).toEqual('2019-11-22');
    });
});