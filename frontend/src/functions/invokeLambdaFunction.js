require('dotenv').config();
let AWS = require('aws-sdk');
AWS.config.region = 'us-east-2';
AWS.config.credentials = new AWS.Credentials(process.env.REACT_APP_AWS_KEY, process.env.REACT_APP_AWS_SECRET);

function invokeLambdaFunction(functionName, payload) {
  return new AWS.Lambda().invoke({
      FunctionName: functionName,
      Payload: JSON.stringify(payload)
    })
    .promise()
    .then(response => JSON.parse(response.Payload.toString()));
}

export { invokeLambdaFunction }