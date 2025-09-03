pipeline {
    agent any
    environment {
        SONAR_TOKEN = credentials('sonar-token') // 'sonar-token' ही तुम्ही credentials मध्ये दिलेली ID आहे
    }
    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn -B clean package'  // Windows मध्ये bat वापर
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('SonarQube Scan') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat "mvn sonar:sonar -Dsonar.projectKey=banking-system -Dsonar.login=%SONAR_TOKEN%"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                waitForQualityGate abortPipeline: true
            }
        }
    }
}
