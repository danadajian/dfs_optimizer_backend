export const getFormattedSalary = (salary: number | undefined) => {
    return salary && '$'.concat(salary.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","))
}