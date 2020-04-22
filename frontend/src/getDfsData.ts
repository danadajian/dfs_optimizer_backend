import {invokeLambdaFunction} from "./aws/aws";
import {formatDate} from "./resources/formateDate/formatDate";

export const getDfsData = async (site: string, sport: string, date: Date) => {
    let dfsData;
    if (site === 'Fanduel') {
        const fanduelData = await invokeLambdaFunction(process.env.REACT_APP_FANDUEL_LAMBDA, {date: formatDate(date)});
        dfsData = fanduelData.filter((contest: any) => contest.sport === sport.toUpperCase());
    } else {
        dfsData = await invokeLambdaFunction(process.env.REACT_APP_DRAFTKINGS_LAMBDA, {sport});
    }
    return dfsData;
};