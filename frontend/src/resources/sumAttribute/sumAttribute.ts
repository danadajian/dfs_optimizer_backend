export const sumAttribute = (lineup: any[], attribute: string) => {
    let attributeArray = lineup.map((player) => ((player[attribute]) ? parseFloat(player[attribute]) : 0));
    return attributeArray.reduce((a, b) => a + b, 0);
};