import {getAdjustedDraftKingsSalary} from "./getAdjustedDraftKingsSalary";
import {getFormattedSalary} from "../getFormattedSalary/getFormattedSalary";

jest.mock('../getFormattedSalary/getFormattedSalary');

(getFormattedSalary as jest.Mock).mockImplementation(() => jest.fn());

describe('getAdjustedDraftKingsSalary', () => {

    describe('is captain case', () => {
        let result: any;
        const salary = 6900;
        const displayPosition = 'Captain (1.5x Points)';

        beforeEach(() => {
            result = getAdjustedDraftKingsSalary(salary, displayPosition);
        })

        it('should return the adjusted salary', () => {
            expect(getFormattedSalary).toHaveBeenCalledWith(salary * 1.5)
        });
    })

    describe('is not captain case', () => {
        let result: any;
        const salary = 6900;
        const displayPosition = 'not the captain';

        beforeEach(() => {
            result = getAdjustedDraftKingsSalary(salary, displayPosition);
        })

        it('should return the original salary', () => {
            expect(getFormattedSalary).toHaveBeenCalledWith(salary)
        });
    })

})