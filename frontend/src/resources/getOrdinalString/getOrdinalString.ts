export const getOrdinalString = (number: number | null) => {
    if (!number) {
        return ''
    }
    let numString = number.toString();
    if (numString.endsWith('1') && numString[numString.length - 2] !== '1') {
        return '(' + numString + 'st)'
    } else if (numString.endsWith('2') && numString[numString.length - 2] !== '1') {
        return '(' + numString + 'nd)'
    } else if (numString.endsWith('3') && numString[numString.length - 2] !== '1') {
        return '(' + numString + 'rd)'
    } else {
        return '(' + numString + 'th)'
    }
};