import {invokeLambdaFunction} from '../functions/invokeLambdaFunction'

let AWS = require('aws-sdk');
let AWSMock = require('aws-sdk-mock');
AWSMock.setSDKInstance(AWS);

describe('test', () => {
    test("invokes lambda and transforms data correctly", async () => {
        AWSMock.mock('Lambda', 'invoke', (params, callback) => {
            callback(null, {Payload: "{\"test\": \"response\"}"});
        });
        const mockLambda = new AWS.Lambda();
        const result = await invokeLambdaFunction(mockLambda, 'testFunction', {test: 'payload'});
        expect(result).toEqual({"test": "response"});

        AWSMock.restore('Lambda');
    })
});