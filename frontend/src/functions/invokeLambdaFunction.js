export function invokeLambdaFunction(lambda, functionName, payload) {
  return lambda.invoke({
      FunctionName: functionName,
      Payload: JSON.stringify(payload)
    })
    .promise()
    .then(response => JSON.parse(response.Payload.toString()));
}