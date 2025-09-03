pipeline {
    agent any

    environment {
        MVN = 'Maven-3.9.9'  // Jenkins मध्ये configure केलेले Maven tool
        SONAR_TOKEN = credentials('SONAR_TOKEN') // Jenkins credentials plugin मध्ये token configure केले
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                bat "${tool MVN}\\bin\\mvn.bat -B clean package"
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
                    bat "${tool MVN}\\bin\\mvn.bat sonar:sonar -Dsonar.projectKey=banking-system -Dsonar.login=${SONAR_TOKEN}"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {  // 5 मिनिटे वाट पाहा
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}
