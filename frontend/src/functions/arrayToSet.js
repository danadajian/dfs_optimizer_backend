export function arrayToSet(array) {
    let set = [];
    for (let i = 0; i < array.length; i++) {
        if (!set.includes(array[i])) {
            set.push(array[i]);
        }
    }
    return set;
}