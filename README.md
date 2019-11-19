# DFS-Optimizer-Web

SAM CLI deployment steps:
1. Build .jar file using maven:
    1. In IntelliJ, go to View > Tool Windows > Maven
    2. Double click "package"
2. Upload latest swagger.yaml to s3:
    1. Run `aws s3 cp swagger.yaml s3://dfs-optimizer/swagger.yaml`
2. Run `sam package --template-file template.yaml --output-template-file packaged.yaml --s3-bucket dfs-optimizer`
3. Run `sam deploy --template-file packaged.yaml --stack-name sam-dfs-optimizer --capabilities CAPABILITY_IAM`
