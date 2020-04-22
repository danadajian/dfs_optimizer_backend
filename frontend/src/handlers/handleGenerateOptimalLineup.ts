import {invokeLambdaFunction} from "../aws/aws";
import {State} from "../interfaces";

export const handleGenerateOptimalLineup = async (state: State, setState: (state: State) => void) => {
    setState({
        ...state,
        isOptimizing: true
    });
    let newLineup;
    let optimalPlayerIds = await invokeLambdaFunction(process.env.REACT_APP_OPTIMAL_LINEUP_LAMBDA, state);
    if (optimalPlayerIds['errorMessage']) {
        alert('An error occurred.' +
        optimalPlayerIds.errorType + '\n' + optimalPlayerIds.errorMessage + '\n' +
        optimalPlayerIds.stackTrace !== undefined ? optimalPlayerIds.stackTrace.slice(0, 7) : '');
    } else if (optimalPlayerIds.length === 0) {
        alert('Failed to generate optimal lineup.')
    } else {
        newLineup = optimalPlayerIds.map((playerId: number) => {
            return state.playerPool.find((player: any) => player.playerId === playerId)
        });
        newLineup.forEach((player: any, index: number) => player.displayPosition = state.displayMatrix[index]);
    }
    setState({
        ...state,
        isOptimizing: false,
        lineup: newLineup || []
    })
};