#!/bin/bash -e

TEST_BUCKET_NAME="kotlin-optimizer-backend"
if aws s3api head-bucket --bucket "${TEST_BUCKET_NAME}" 2>/dev/null
then
    echo "Bucket exists: $TEST_BUCKET_NAME"
else
    echo "Bucket does not exist, creating: ${TEST_BUCKET_NAME}"
    aws s3 mb s3://"${TEST_BUCKET_NAME}"
    aws s3api put-bucket-policy --bucket "${TEST_BUCKET_NAME}" --policy file://./bucket-policy.json
#    aws s3 website "s3://${BUCKET_NAME}" --index-document index.html
fi

PATH_TO_FILE=$(find . -name "*-jar-with-dependencies.jar*" | head -n 1)

echo "### Initiating SAM Deploy..."

aws s3 rm "s3://${TEST_BUCKET_NAME}" --recursive --exclude "*" --include "*.jar"
aws s3 cp "${PATH_TO_FILE}" "s3://${TEST_BUCKET_NAME}/"
FILE_NAME="${PATH_TO_FILE:9}"

STACK_NAME="kotlin-optimizer-stack"

sam --version
sam deploy --template-file ./template.yaml --stack-name "${STACK_NAME}" --capabilities CAPABILITY_IAM \
  --parameter-overrides BucketName="${TEST_BUCKET_NAME}" CodeKey="${FILE_NAME}" ApiKey="${API_KEY}" \
  ApiSecret="${API_SECRET}" AwsKey="${AWS_ACCESS_KEY_ID}" AwsSecret="${AWS_SECRET_ACCESS_KEY}" \
   --no-fail-on-empty-changeset