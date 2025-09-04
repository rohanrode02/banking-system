pipeline {
    agent any
    tools {
        maven 'Maven-3.9.9'
        jdk 'JDK17'
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
        stage('Deploy to Tomcat') {
            steps {
                deploy adapters: [tomcat9(credentialsId: 'tomcat-cred-id',
                                          path: '',
                                          url: 'http://localhost:8091')],
                       contextPath: '/banking-system',
                       war: 'target/banking-system-0.0.1-SNAPSHOT.war'
            }
        }
    }
}
