import {getFormattedSalary} from "../getFormattedSalary/getFormattedSalary";

export const getAdjustedDraftKingsSalary = (salary: number, displayPosition: string) => {
    let adjustedSalary = salary;
    const isCaptain = displayPosition.includes('x Points)');
    if (salary && isCaptain) {
        let multiplier = (parseFloat(displayPosition.split('(')[1].substring(0, 3)));
        adjustedSalary = salary * multiplier;
    }
    return getFormattedSalary(adjustedSalary);
}