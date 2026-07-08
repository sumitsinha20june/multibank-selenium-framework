pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    parameters {
        choice(name: 'SUITE', choices: ['local', 'dweb', 'mweb', 'regression', 'cross-browser'], description: 'TestNG suite to run')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browsers in headless mode')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                sh "mvn -q clean test -Dheadless=${params.HEADLESS} -DsuiteXmlFile=src/test/resources/suites/${params.SUITE}.xml"
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/junitreports/*.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: 'test-output/**, target/allure-results/**, target/surefire-reports/**'
        }
    }
}
