import {getDfsData} from "./getDfsData";
import {extractContestsFromDfsData} from "./resources/extractContestsFromDfsData/extractContestsFromDfsData";
import {invokeLambdaFunction} from "./aws/aws";
import {State} from "./State";
import {INITIAL_STATE} from "./constants";

export const collectDataAndGetNewState = async (sport: string, state: State, setState: (state: State) => void) => {
    const updateLoadingText = (loadingText: string) => {
        setState({
            ...INITIAL_STATE,
            site: state.site,
            isLoading: true,
            loadingText,
            sport
        });
    };
    updateLoadingText(`${state.site} data`);
    const dfsData = await getDfsData(state.site, sport, state.date);
    const contests = extractContestsFromDfsData(dfsData, state.site, state.date);
    updateLoadingText(sport.toUpperCase() + ' projections');
    const projectionsData = await invokeLambdaFunction(process.env.REACT_APP_PROJECTIONS_LAMBDA, {sport});
    updateLoadingText(sport.toUpperCase() + ' opponent ranks');
    const opponentRanks = await invokeLambdaFunction(process.env.REACT_APP_OPPONENT_RANKS_LAMBDA, {sport});
    updateLoadingText(sport.toUpperCase() + ' injury data');
    const injuries = await invokeLambdaFunction(process.env.REACT_APP_INJURIES_LAMBDA, {sport});
    let playerStatuses = [];
    if (sport === 'nhl') {
        updateLoadingText('player statuses');
        playerStatuses = await invokeLambdaFunction(process.env.REACT_APP_GOALIE_SCRAPER_LAMBDA);
    }
    return {
        ...state,
        isLoading: false,
        sport,
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
    };
};
