#!/bin/bash -e

if aws s3api head-bucket --bucket "${BUCKET_NAME}" 2>/dev/null
then
    echo "Bucket exists: $BUCKET_NAME"
else
    echo "Bucket does not exist, creating: ${BUCKET_NAME}"
    aws s3 mb s3://"${BUCKET_NAME}"
    aws s3api put-bucket-policy --bucket "${BUCKET_NAME}" --policy file://./bucket-policy.json
    aws s3 website "s3://${BUCKET_NAME}" --index-document index.html
fi

PATH_TO_FILE=$(find . -name "*-jar-with-dependencies.jar*" | head -n 1)

echo "### Initiating SAM Deploy..."

aws s3 rm "s3://${BUCKET_NAME}" --recursive --exclude "*" --include "*.jar"
aws s3 cp "${PATH_TO_FILE}" "s3://${BUCKET_NAME}/"
FILE_NAME="${PATH_TO_FILE:9}"

STACK_NAME="dfs-optimizer-stack"

sam --version
sam deploy --template-file ./template.yaml --stack-name "${STACK_NAME}" --capabilities CAPABILITY_IAM \
  --parameter-overrides BucketName="${BUCKET_NAME}" CodeKey="${FILE_NAME}" ApiKey="${API_KEY}" \
  ApiSecret="${API_SECRET}" AwsKey="${AWS_ACCESS_KEY_ID}" AwsSecret="${AWS_SECRET_ACCESS_KEY}" \
   --no-fail-on-empty-changeset