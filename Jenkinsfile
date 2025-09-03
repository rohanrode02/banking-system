pipeline {
    agent any

    tools {
        maven 'Maven-3.9.0' // Jenkins Maven tool name
        jdk 'JDK-17'       // Jenkins JDK tool name
    }

    environment {
        SONAR_TOKEN = credentials('sonar-token-id') // Jenkins credentials id
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/rohanrode02/banking-system.git', branch: 'main'
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn clean package'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat "mvn sonar:sonar -Dsonar.projectKey=banking-system -Dsonar.login=%SONAR_TOKEN%"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') { // 5 मिनिटांत status न आल्यास fail
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}
