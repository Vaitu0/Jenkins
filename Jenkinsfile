@Library('library') _

def pipelineConfig = readJSON text: libraryResource('pipeline_config.json')

pipeline {
     agent {
         label 'agent'
     }
     
     environment {
         PIP_BREAK_SYSTEM_PACKAGES = 1
         scannerHome = tool 'SonarQube'
     }
     
     stages {
         stage('Get Code') {
             steps {
                 checkout scm
             }
         }
         stage('Unit tests') {
             steps {
                 runTests()
             }
         }
         stage('Sonarqube analysis') {
             steps {
                 runSonarAnalysis(scannerHome)
             }
         }
         stage('Build application image') {
             steps {
                 script {
                     dockerTag = "RC-${env.BUILD_ID}.${env.GIT_COMMIT.take(7)}"
                     applicationImage = buildImage(pipelineConfig.imageName, dockerTag)
                 }
             }
         }
         stage ('Pushing image to Artifactory') {
             steps {
                 script {
                     pushToRegistry(applicationImage, pipelineConfig.registryCredentials, pipelineConfig.dockerRegistry)
                 }
             }
         }
     }
     
     post {
         always {
             junit testResults: "test-results/*.xml"
             cleanWs()
         }
     }
}
