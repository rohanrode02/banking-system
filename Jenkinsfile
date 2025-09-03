pipeline {
  agent any
  environment {
    // Jenkins मध्ये configure केलेले Maven tool name
    MVN = "Maven-3.8.8"
  }
  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Test') {
      steps {
        withMaven(maven: "${MVN}") {
          sh 'mvn -B clean package'   // Windows: use bat instead of sh
        }
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
        // withSonarQubeEnv requires SonarQube server configured in Jenkins (Manage Jenkins -> Configure System)
        withSonarQubeEnv('SonarQube') {
          sh "mvn sonar:sonar -Dsonar.projectKey=banking-system -Dsonar.login=${SONAR_TOKEN}"
        }
      }
    }

    stage('Quality Gate') {
      steps {
        // requires 'waitForQualityGate' step provided by Sonar Jenkins plugin
        waitForQualityGate abortPipeline: true
      }
    }
  }
}
