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

aws cloudfront create-invalidation --distribution-id "${CDN_DISTRIBUTION_ID}" --paths /\*
aws s3 sync frontend/build "s3://${BUCKET_NAME}" --exclude "precache-manifest*"