import { getOrdinalString } from '../functions/getOrdinalString'

describe('gets correct ordinal string', () => {
    test('gets correct ordinal string', () => {
        expect(getOrdinalString(1)).toEqual('(1st)');
        expect(getOrdinalString(2)).toEqual('(2nd)');
        expect(getOrdinalString(3)).toEqual('(3rd)');
        expect(getOrdinalString(4)).toEqual('(4th)');
        expect(getOrdinalString(11)).toEqual('(11th)');
        expect(getOrdinalString(12)).toEqual('(12th)');
        expect(getOrdinalString(13)).toEqual('(13th)');
        expect(getOrdinalString(21)).toEqual('(21st)');
        expect(getOrdinalString(22)).toEqual('(22nd)');
        expect(getOrdinalString(23)).toEqual('(23rd)');
        expect(getOrdinalString(32)).toEqual('(32nd)');
        expect(getOrdinalString(null)).toEqual('');
    });
});