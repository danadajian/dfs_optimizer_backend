#!/bin/bash -e

source config.sh

MVN_VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -Dexpression=project.version -q -DforceStdout)
MAJOR_VERSION=${MVN_VERSION:0:1}
MINOR_VERSION=${MVN_VERSION:2:2}
mvn replacer:replace -Dccih.origin="<version>${MVN_VERSION}</version>" -Dccih.target="<version>${MAJOR_VERSION}.$((MINOR_VERSION + 1))</version>"
mvn clean package

BUCKET_NAME="dfsoptimizer.app"
STACK_NAME="dfs-optimizer-stack"

if aws s3api head-bucket --bucket ${BUCKET_NAME} 2>/dev/null
then
    echo "Bucket exists: $BUCKET_NAME"
else
    echo "Bucket does not exist, creating: ${BUCKET_NAME}"
    aws s3 mb s3://${BUCKET_NAME}
    aws s3api put-bucket-policy --bucket ${BUCKET_NAME} --policy file://./bucket-policy.json
    aws s3 website "s3://${BUCKET_NAME}" --index-document index.html
fi

PATH_TO_FILE=$(find . -name "*-jar-with-dependencies.jar*" | head -n 1)

echo "### SAM Deploy"

aws s3 rm "s3://${BUCKET_NAME}" --recursive --exclude "*" --include "*.jar"
aws s3 cp "${PATH_TO_FILE}" "s3://${BUCKET_NAME}/"
FILE_NAME="${PATH_TO_FILE:9}"
aws s3 cp ./swagger.yaml "s3://${BUCKET_NAME}/"

if [[ "$OSTYPE" == "msys" ]]; then
    sam.cmd --version
    sam.cmd deploy --template-file ./template.yaml --stack-name ${STACK_NAME} --capabilities CAPABILITY_IAM \
     --parameter-overrides BucketName=${BUCKET_NAME} CodeKey="${FILE_NAME}" ApiKey="${API_KEY}" ApiSecret="${API_SECRET}" \
      --no-fail-on-empty-changeset
else
    sam --version
    sam deploy --template-file ./template.yaml --stack-name ${STACK_NAME} --capabilities CAPABILITY_IAM \
     --parameter-overrides BucketName=${BUCKET_NAME} CodeKey="${FILE_NAME}" ApiKey="${API_KEY}" ApiSecret="${API_SECRET}" \
      --no-fail-on-empty-changeset
fi

echo "### Building frontend"
cd frontend
npm test
npm run build

cd build
aws s3 sync . "s3://dfsoptimizer.app/" --exclude "precache-manifest*"

echo "BUILD COMPLETE!"