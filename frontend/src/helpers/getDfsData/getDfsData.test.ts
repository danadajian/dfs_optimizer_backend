import {getDfsData} from './getDfsData'
import {invokeLambdaFunction} from "../../aws/aws";
import {formatDate} from "../formatDate/formatDate";

jest.mock('../../aws/aws');
jest.mock('../formatDate/formatDate');

(invokeLambdaFunction as jest.Mock).mockResolvedValue([
    {
        sport: 'blah'
    },
    {
        sport: 'A SPORT'
    },
    {
        sport: 'a sport'
    }
]);
(formatDate as jest.Mock).mockReturnValue('formatted date');
jest.spyOn(global, 'Date');

describe('get dfs data', () => {
    describe('fanduel case', () => {
        let result: any;
        const site = 'Fanduel';
        const sport = 'a sport';
        const date = new Date();

        beforeEach(async () => {
            result = await getDfsData(site, sport, date)
        });

        it('should call invoke lambda with correct params', () => {
            expect(invokeLambdaFunction).toHaveBeenCalledWith(process.env.REACT_APP_FANDUEL_LAMBDA, {date: 'formatted date'})
        });

        it('should return filtered data', () => {
            expect(result).toEqual([{sport: 'A SPORT'}])
        });
    });

    describe('draftkings case', () => {
        let result: any;
        const site = 'DraftKings';
        const sport = 'a sport';
        const date = new Date();

        beforeEach(async () => {
            result = await getDfsData(site, sport, date)
        });

        it('should call invoke lambda with correct params', () => {
            expect(invokeLambdaFunction).toHaveBeenCalledWith(process.env.REACT_APP_FANDUEL_LAMBDA, {date: 'formatted date'})
        });

        it('should return expected result',  () => {
            expect(result).toEqual([
                {
                    sport: 'blah'
                },
                {
                    sport: 'A SPORT'
                },
                {
                    sport: 'a sport'
                }
            ])
        });
    });
});