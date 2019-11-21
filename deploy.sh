#!/bin/bash -e

source config.sh

mvn clean package

BUCKET_NAME=dfs-optimizer-stack

if aws s3api head-bucket --bucket ${BUCKET_NAME} 2>/dev/null
then
    echo "Bucket exists: $BUCKET_NAME"
else
    echo "Bucket does not exist, creating: $BUCKET_NAME"
    aws s3 mb s3://${BUCKET_NAME}
fi

PATH_TO_FILE=$(find . -name *-jar-with-dependencies.jar* | head -n 1)
echo PATH_TO_FILE:
echo ${PATH_TO_FILE}

echo "### SAM Deploy"

aws s3 cp ${PATH_TO_FILE} "s3://${BUCKET_NAME}/"
FILE_NAME="${PATH_TO_FILE:9}"
aws s3 cp ./swagger.yaml "s3://${BUCKET_NAME}/"

if [[ "$OSTYPE" == "msys" ]]; then
    sam.cmd --version
    sam.cmd deploy --template-file ./template.yaml --stack-name ${BUCKET_NAME} --capabilities CAPABILITY_IAM \
     --parameter-overrides BucketName=${BUCKET_NAME} CodeKey=${FILE_NAME} ApiKey=${API_KEY} ApiSecret=${API_SECRET} \
      --no-fail-on-empty-changeset
else
    sam --version
    sam deploy --template-file ./template.yaml --stack-name ${BUCKET_NAME} --capabilities CAPABILITY_IAM \
     --parameter-overrides BucketName=${BUCKET_NAME} CodeKey=${FILE_NAME} ApiKey=${API_KEY} ApiSecret=${API_SECRET} \
      --no-fail-on-empty-changeset
fi