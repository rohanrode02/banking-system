pipeline {
    agent any
    tools {
        maven 'Maven-3.9.9'   // Jenkins Maven tool name
        jdk 'JDK17'           // Jenkins JDK tool name
    }
    environment {
        SONAR_TOKEN = credentials('sonar-token-id')
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/rohanrode02/banking-system.git'
            }
        }
        stage('Build & Test') {
            steps {
                bat 'mvn clean package'
                junit '**/target/surefire-reports/*.xml'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat "mvn sonar:sonar -Dsonar.projectKey=banking-system -Dsonar.login=${env.SONAR_TOKEN}"
                }
            }
        }
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}
