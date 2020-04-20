import {invokeLambdaFunction} from './aws'
import {Lambda} from "../aws";

jest.mock('../aws');

(Lambda.invoke as jest.Mock).mockImplementation(() => {
    return {
        promise: jest.fn(() => {
            return {
                Payload: Buffer.from(JSON.stringify({some: 'stuff'}))
            }
        })
    }
});

describe('invoke lambda function', () => {
    let result: any;
    beforeEach(async () => {
        result = await invokeLambdaFunction('function name', {some: 'payload'});
    });

    it("calls lambda invoke with correct params", () => {
        const params = {
            FunctionName: 'function name',
            Payload: JSON.stringify({some: 'payload'})
        };
        expect(Lambda.invoke).toHaveBeenCalledWith(params);
    });

    it('should return the expected result', () => {
        expect(result).toEqual({some: 'stuff'})
    });
});