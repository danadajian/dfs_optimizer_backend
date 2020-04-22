export const getSetFromArray = (array: any[]) => {
    let set: any[] = [];
    for (let i = 0; i < array.length; i++) {
        if (!set.includes(array[i])) {
            set.push(array[i]);
        }
    }
    return set;
};