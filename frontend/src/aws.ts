import * as AWS from 'aws-sdk';
import './env'

// @ts-ignore
AWS.config.credentials = new AWS.Credentials(process.env.REACT_APP_AWS_KEY, process.env.REACT_APP_AWS_SECRET);
export const Lambda = new AWS.Lambda({region: 'us-east-2'});
