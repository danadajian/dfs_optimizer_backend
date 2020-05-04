import {Lambda} from "../aws";
import {isDevelopment} from "../constants";

const mockFanduelData = require('../fixtures/fanduelDataResponse.json');
const mockProjectionsData = require('../fixtures/nflProjectionsResponse.json');
const mockOpponentRanksData = require('../fixtures/opponentRanksResponse.json');
const mockInjuriesData = require('../fixtures/nflInjuriesResponse.json');
const mockOptimalLineupResponse = require('../fixtures/optimalLineupResponse.json');

export const invokeLambdaFunction = async (functionName: any, payload = {}) => {
    const params = {
        FunctionName: functionName,
        Payload: JSON.stringify(payload)
    };
    if (isDevelopment()) {
        return mockResponseMap[functionName]
    } else {
        const response: any = await Lambda.invoke(params).promise();
        return JSON.parse(response.Payload.toString());
    }
};

const mockResponseMap: any = {
    'dfs-optimizer-stack-GetFanduelDataFunction-77GMJJP3YXN8': mockFanduelData,
    'dfs-optimizer-stack-GetProjectionsDataFunction-QKLNCL4DUKM2': mockProjectionsData,
    'dfs-optimizer-stack-GetOpponentRanksDataFunction-9KIEIP392WEF': mockOpponentRanksData,
    'dfs-optimizer-stack-GetInjuryDataFunction-NBYNB8ZSZBW6': mockInjuriesData,
    'dfs-optimizer-stack-GetOptimalLineupFunction-12U6F4GY6ANQ2': mockOptimalLineupResponse
}