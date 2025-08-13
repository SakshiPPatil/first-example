# Deploy the application springboot application using github action on ec2 instance

1) create the springboot application on local machine. <br/>
2)  note: pom.xml file is important(in this file it shows the connectivity to the database, jar filename which is created the after the maven run).<br/>
3)  resources-> application.properties-> provide the aurora database(url),username,password.<br/>
4)  push all code on the github repo.<br/>
5)  after create the workflow file(github action) its path is .github/workflow/main.yml: (main.yml is workflow file)<br/>
 **steps:** <br/>
   i) define the actions like which os is used.<br/>
   ii) define the environment variables.<br/>
   iii) setup the java (setup jdk-17) and maven.<br/>
   iv) build the maven<br/>
   
   ```
   mvn clean package -DskipTests
   ```
      
   It creates the jar file. and the location of jar file in local machine is target/jarfile_name. <br/>

  6)  Run unit and integration tests. <br/>
  
  ```
    mvn test
  ```
   It checks the java application syntax,error <br/>


  7) Copy  jar file to EC2(use ssh action) . <br/>
  8) Verify the file was copied to the EC2 instance(use ssh action). <br/>
  9) Deploy to EC2(use ssh action) <br/>

this is the workflow file and  path is .github/workflow/filename.yml<br/>
```
# File: .github/workflows/deploy-to-ec2.yml
# This workflow deploys a Spring Boot application to an AWS EC2 instance.
# It includes running tests and using environment variables for the database.
name: Deploy Spring Boot App to EC2 (with Aurora)

on:
  push:
    branches:
      - main

jobs:
  deploy:
    name: Deploy to EC2
    runs-on: ubuntu-latest

    # Environment variables for the deployment process.
    env:
      EC2_HOST: ${{ secrets.EC2_HOST }}
      EC2_USER: "ubuntu"
      APP_JAR_NAME: "demo-0.0.1-SNAPSHOT.jar"
      EC2_APP_PATH: "/home/ubuntu/app"
      # Pass database secrets directly as environment variables for the SSH action
      DB_URL: ${{ secrets.DB_URL }}
      DB_USER: ${{ secrets.DB_USER }}
      DB_PASS: ${{ secrets.DB_PASS }}

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: 'maven'

      # Step 1: Build the application and verify the JAR file exists.
      - name: Build with Maven and Verify JAR
        run: |
          echo "Building application with Maven..."
          mvn clean package -DskipTests
          if [ ! -f "target/${{ env.APP_JAR_NAME }}" ]; then
            echo "❌ Error: JAR file was not found after the build."
            exit 1
          fi
          echo "✅ JAR file found."

      # Diagnostic step: List files in the target directory to confirm JAR exists.
      - name: List target directory contents
        run: |
          ls -R target/

      # Step 2: Run unit and integration tests.
      - name: Run unit and integration tests
        run: |
          echo "Running Maven tests..."
          mvn test

      # New step: Prepare the EC2 app directory for file transfer.
      # Step 3: Transfer the JAR file to the EC2 instance.
      - name: Copy file to EC2
        uses: appleboy/scp-action@master
        with:
          host: ${{ env.EC2_HOST }}
          username: ${{ env.EC2_USER }}
          key: ${{ secrets.EC2_SSH_KEY }}
          source: "target/${{ env.APP_JAR_NAME }}"
          target: ${{ env.EC2_APP_PATH }}

      # New step: Verify the file was copied to the EC2 instance.
      - name: Verify JAR on EC2
        uses: appleboy/ssh-action@v1.0.3
        with:
          host: ${{ env.EC2_HOST }}
          username: ${{ env.EC2_USER }}
          key: ${{ secrets.EC2_SSH_KEY }}
          script: |
            echo "Verifying JAR file on EC2..."
            echo "Contents of ${{ env.EC2_APP_PATH }}:"
            ls -la ${{ env.EC2_APP_PATH }}
            if [ ! -f "${{ env.EC2_APP_PATH }}/target/${{ env.APP_JAR_NAME }}" ]; then
              echo "❌ Error: The JAR file was not found on the EC2 instance after transfer."
              exit 1
            fi
            echo "✅ JAR file successfully transferred to EC2."

      # Step 4: SSH into the EC2 instance and run deployment commands.
      - name: Deploy to EC2
        uses: appleboy/ssh-action@v1.0.3
        with:
          host: ${{ env.EC2_HOST }}
          username: ${{ env.EC2_USER }}
          key: ${{ secrets.EC2_SSH_KEY }}
          # Pass the DB credentials from the workflow's `env` section to the script as environment variables.
          envs: DB_URL,DB_USER,DB_PASS
          # The script to run on the EC2 instance.
          script: |
            # Navigating to the app directory on EC2.
            echo "Navigating to app directory on EC2..."
            cd /home/ubuntu/app/target

            # Check if the JAR file exists on the EC2 instance.
            if [ ! -f "demo-0.0.1-SNAPSHOT.jar" ]; then
              echo "❌ Error: The JAR file was not found on the EC2 instance."
              exit 1
            fi

            echo "✅ JAR file found on EC2. Stopping old process and starting new one..."
            # Stop any existing process running on port 8080. The `|| true` prevents the script from failing if no process is found.
            sudo fuser -k 8080/tcp || true

            # Explicitly use the 'java' executable located inside the $JAVA_HOME/bin directory.
            # The `nohup` command is used to run the process in the background, detached from the terminal session.
            # We now use the environment variables passed via the `envs` input to the action.
            nohup "$JAVA_HOME"/bin/java \
              -Dspring.datasource.url="$DB_URL" \
              -Dspring.datasource.username="$DB_USER" \
              -Dspring.datasource.password="$DB_PASS" \
              -jar demo-0.0.1-SNAPSHOT.jar > application.log 2>&1 &

            # Wait for 10 seconds to allow the application to start.
            sleep 10

            # Check the status of the application by seeing if anything is listening on port 8080.
            if sudo ss -ltnp | grep :8080; then
              echo "✅ Application successfully started on port 8080."
            else
              echo "❌ Application failed to start. Printing logs..."
              # If it failed, print the application log for debugging.
              cat application.log
              exit 1
            fi

            echo "Deployment finished."
```


10) before create the workflow file add the variaables in secretes and variables in github repo   in setting->sercets and variables add the following variables: <br/>

```
  DB_URL  ---->jdbc:mysql://backup.cluster-c854mo04o1n3.us-east-1.rds.amazonaws.com:3306/your_database_name
  DB_USER
  DB_PASS
  EC2_HOST
  SSH_KEY   ---> provide the content of the ssh private key
  EC2_USER                  REFER FROM THE SECRETS AND VARIABLES

```

11) create the ec2 instance and install jdk-17 jre-17 and maven and mysql <br/>
12) create the aurora database and provide the endpoint in the application.properties and in the secrets in the repository. <br/>
13) host the application public_ip_instance:8080 <br/>
14) connect the aurora database to the instance using command <br/>
 ```
mysql -h endpoint_of _aurora_database -P 3306 -u username -p password     insert the values in the database.
```
