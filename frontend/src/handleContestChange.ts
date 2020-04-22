import {LINEUP_RULES} from "./constants";
import {combineDfsAndProjectionsData} from "./resources/combineDfsAndProjectionsData/combineDfsAndProjectionsData";
import {createEmptyLineup} from "./resources/createEmptyLineup/createEmptyLineup";
import {State} from "./State";

export const handleContestChange = (contest: string, state: State, setState: (state: State) => void) => {
    const {site, sport, dfsData, projectionsData, opponentRanks, injuries, playerStatuses} = state;
    if (!contest)
        return;
    if (dfsData.length === 0) {
        alert(site + ' data is currently unavailable.');
        return
    }
    if (Object.keys(projectionsData).length === 0 || projectionsData['errorMessage']) {
        alert('Player projection data is currently unavailable.');
        return
    }
    const gameType = contest.includes('@') || contest.includes('vs') ? 'Single Game' : 'Classic';
    const contestRules = LINEUP_RULES[site][sport][gameType];
    const {lineupPositions, displayMatrix, salaryCap, lineupRestrictions} = contestRules;
    const dfsPlayers = dfsData.filter((contestJson: any) => contestJson.contest === contest)[0]['players'];
    const playerPool = combineDfsAndProjectionsData(dfsPlayers, projectionsData, site, opponentRanks, injuries,
        playerStatuses);
    const emptyLineup: any = createEmptyLineup(contestRules.lineupPositions, contestRules.displayMatrix);
    setState({
        ...state,
        contest,
        playerPool,
        filteredPool: [],
        whiteList: [],
        blackList: [],
        lineup: emptyLineup,
        lineupPositions,
        displayMatrix,
        salaryCap,
        lineupRestrictions
    });
};