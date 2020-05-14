import {getFormattedSalary} from "./getFormattedSalary";

describe('getFormattedSalary', () => {
    describe('salary exists case', () => {
        let result: any;
        const salary = 6900;

        beforeEach(() => {
            result = getFormattedSalary(salary)
        })

        it('should return formatted salary', () => {
            expect(result).toEqual('$6,900')
        });
    });

    describe('salary does not exist case', () => {
        let result: any;
        const salary = undefined;

        beforeEach(() => {
            result = getFormattedSalary(salary)
        })

        it('should return formatted salary', () => {
            expect(result).toEqual(undefined)
        });
    });
})