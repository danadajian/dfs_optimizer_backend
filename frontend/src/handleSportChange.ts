import {getDfsData} from "./getDfsData";
import {extractContestsFromDfsData} from "./resources/extractContestsFromDfsData/extractContestsFromDfsData";
import {invokeLambdaFunction} from "./aws/aws";

export const loadAllDataAndGetNewState = async (site: string, sport: string, date: Date) => {
    const dfsData = await getDfsData(site, sport, date);
    const contests = extractContestsFromDfsData(dfsData, site, date);
    // updateLoadingText(sport.toUpperCase() + ' projections');
    const projectionsData = await invokeLambdaFunction(process.env.REACT_APP_PROJECTIONS_LAMBDA, {sport});
    // updateLoadingText(sport.toUpperCase() + ' opponent ranks');
    const opponentRanks = await invokeLambdaFunction(process.env.REACT_APP_OPPONENT_RANKS_LAMBDA, {sport});
    // updateLoadingText(sport.toUpperCase() + ' injury data');
    const injuries = await invokeLambdaFunction(process.env.REACT_APP_INJURIES_LAMBDA, {sport});
    let playerStatuses = [];
    if (sport === 'nhl') {
        // updateLoadingText('player statuses');
        playerStatuses = await invokeLambdaFunction(process.env.REACT_APP_GOALIE_SCRAPER_LAMBDA);
    }
    return {
        isLoading: false,
        dfsData,
        contests,
        projectionsData: projectionsData.body,
        opponentRanks,
        injuries,
        playerStatuses,
        playerPool: [],
        filteredPool: [],
        whiteList: [],
        blackList: []
    }
};