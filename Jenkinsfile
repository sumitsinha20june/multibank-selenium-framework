pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    parameters {
        choice(name: 'SUITE', choices: ['local', 'dweb', 'mweb', 'regression', 'cross-browser'], description: 'TestNG suite to run')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browsers in headless mode')
        booleanParam(name: 'DOCKER_GRID', defaultValue: false, description: 'Start Selenium Grid via Docker Compose (requires Docker)')
    }

    stages {
        stage('Start Selenium Grid') {
            when { expression { params.DOCKER_GRID } }
            steps {
                sh "docker compose -f ${WORKSPACE}/docker/docker-compose.yml up -d"
                sleep 30
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -q clean install -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh "mvn -q clean test -Dheadless=${params.HEADLESS} -DsuiteXmlFile=src/test/resources/suites/${params.SUITE}.xml"
            }
        }

        stage('Stop Selenium Grid') {
            when { expression { params.DOCKER_GRID } }
            steps {
                sh "docker compose -f ${WORKSPACE}/docker/docker-compose.yml down"
            }
        }
    }

    post {
        always {
            testng allowEmptyResults: true, testResults: 'target/surefire-reports/testng-results.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: 'test-output/**, target/allure-results/**, target/surefire-reports/**'
        }

        success {
            emailext(
                to: 'sumit@test.com',
                subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
<html>
<body>
<p>Hello Team,</p>
<p>The latest Jenkins build has completed successfully.</p>
<p><b>Project Name:</b> ${env.JOB_NAME}</p>
<p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
<p><b>Build Status:</b> <span style="color: green;"><b>SUCCESS</b></span></p>
<p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
<p>Best regards,</p>
<p><b>Automation Team</b></p>
</body>
</html>""",
                mimeType: 'text/html',
                attachLog: true
            )
        }

        failure {
            emailext(
                to: 'sumit@test.com',
                subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
<html>
<body>
<p>Hello Team,</p>
<p>The latest Jenkins build has <b style="color: red;">FAILED</b>.</p>
<p><b>Project Name:</b> ${env.JOB_NAME}</p>
<p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
<p><b>Build Status:</b> <span style="color: red;"><b>FAILED</b></span></p>
<p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
<p><b>Please check the logs and take necessary actions.</b></p>
<p>Best regards,</p>
<p><b>Automation Team</b></p>
</body>
</html>""",
                mimeType: 'text/html',
                attachLog: true
            )
        }
    }
}
