import {Lambda} from "../aws";

export const invokeLambdaFunction = async (functionName: any, payload = {}) => {
    const params = {
        FunctionName: functionName,
        Payload: JSON.stringify(payload)
    };
    const response: any = await Lambda.invoke(params).promise();
    return JSON.parse(response.Payload.toString());
};