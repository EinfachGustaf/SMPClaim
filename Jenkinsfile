pipeline {
  agent any
  tools {
    gradle 'Gradle 8.6'
    jdk 'jdk17'
  }
  environment {
    DISCORD_WEBHOOK_URL = credentials('discord-webhook')
  }
  stages {
    stage ('Initialize') {
      when {
        not { anyOf { branch 'dependabot/**'; branch 'legacy/**' } }
      }
      steps {
        sh '''
          echo "PATH = ${PATH}"
          echo "M2_HOME = ${M2_HOME}"
        '''
      }
    }
    stage('Build') {
      when {
        not { anyOf { branch 'dependabot/**'; branch 'legacy/**' } }
      }
      steps {
        sh 'chmod +x gradlew'
        sh './gradlew clean build --stacktrace --warning-mode=fail'
        archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
        //discordSend description: "Jenkins Pipeline Build", footer: "Jenkins @ EinfachGustaf.live", link: env.BUILD_URL, result: currentBuild.currentResult, title: JOB_NAME, webhookURL: "${DISCORD_WEBHOOK_URL}"
      }
    }
  }
}