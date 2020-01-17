function extractContestsFromData(dataArray, site, date) {
  let contestArray = [];
  if (site === 'fd')
    dataArray.forEach(contestJson => contestArray.push(contestJson.contest));
  else
    dataArray
      .filter(contestJson => {
        let contestName = contestJson.contest;
        let contestDate = contestName.split(' ')[contestName.split(' ').length - 1].slice(1, -1);
        let month = parseInt(contestDate.split('/')[0]);
        let day = parseInt(contestDate.split('/')[1]);
        return date.getMonth() + 1 === month && date.getDate() === day;
      })
      .forEach(contestJson => contestArray.push(contestJson.contest));
  return contestArray;
}

export { extractContestsFromData }