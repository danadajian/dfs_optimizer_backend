#!/bin/bash -e

source ./config.sh

echo "### Building frontend..."

BUCKET_NAME="dfsoptimizer.app"

cd frontend
if ! npm test
then
  echo "Failed frontend tests!"
  exit
fi
npm run build

echo "### Deploying frontend..."

aws cloudfront create-invalidation --distribution-id "${CDN_DISTRIBUTION_ID}" --paths /\*
cd build
aws s3 sync . "s3://${BUCKET_NAME}" --exclude "precache-manifest*"

echo "Build complete! :)"