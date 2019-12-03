import { arrayToSet } from '../functions/arrayToSet'

describe('converts array to set', () => {
    test('converts array to set', () => {
        let array = [1, 2, 2, 3, 1, 5, 8, 5];
        expect(arrayToSet(array)).toEqual([1, 2, 3, 5, 8]);
    });
});