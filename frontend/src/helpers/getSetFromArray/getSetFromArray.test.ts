import { getSetFromArray } from './getSetFromArray'

describe('converts array to set', () => {
    it('converts array to set', () => {
        let array = [1, 2, 2, 3, 1, 5, 8, 5];
        expect(getSetFromArray(array)).toEqual([1, 2, 3, 5, 8]);
    });
});