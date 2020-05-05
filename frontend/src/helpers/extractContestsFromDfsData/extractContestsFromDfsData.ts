export const extractContestsFromDfsData = (dataArray: any[], site: string, date: Date) => {
    let contestArray: any[] = [];
    if (site === 'Fanduel')
        dataArray.forEach((contestJson: any) => contestArray.push(contestJson.contest));
    else {
        dataArray &&
        dataArray
            .filter((contestJson: any) => {
                const contestName = contestJson.contest;
                const contestNameWords = contestName.split(' ');
                const contestDate = contestNameWords[contestNameWords.length - 1].slice(1, -1);
                const month = parseInt(contestDate.split('/')[0]);
                const day = parseInt(contestDate.split('/')[1]);
                return date.getMonth() + 1 === month && date.getDate() === day;
            })
            .forEach((contestJson: any) => contestArray.push(contestJson.contest));
    }
    return contestArray;
};