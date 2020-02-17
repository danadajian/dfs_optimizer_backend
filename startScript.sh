#!/bin/bash -e

JAR_FILE_NAME=$(AWS_ACCESS_KEY_ID=${AWS_KEY} AWS_SECRET_ACCESS_KEY=${AWS_SECRET} \
 aws s3api list-objects --bucket dfsoptimizer.app --query \
 'Contents[?starts_with(Key, `DFS-Optimizer-Web-`) == `true`].Key' | jq -r '.[0]')

echo "$JAR_FILE_NAME"

AWS_ACCESS_KEY_ID=${AWS_KEY} AWS_SECRET_ACCESS_KEY=${AWS_SECRET} aws s3 cp "s3://dfsoptimizer.app/${JAR_FILE_NAME}" .

java -cp "$JAR_FILE_NAME" main.Optimizer