pipeline {
  environment {
    registry = 'dekabitasp/mystok-jenkins-test'
    registryCredential = 'dockerhub'
  }
  agent none
  stages {
    stage('Build war') {
      agent {
        docker {
          image 'maven:3-alpine'
          args '-v /root/.m2:/root/.m2'
        }
      }
      steps {
        sh 'mvn -B -DskipTests clean package'
      }
    }
    stage('Build Container Image') {
      agent any
      steps {
        script {
          dockerImage = docker.build registry + ":$BUILD_NUMBER"
        }
      }
    }
    stage('Deploy Image') {
      agent any
      steps {
        script {
          docker.withRegistry('',registryCredential) {
            dockerImage.push()
            dockerImage.push('latest')
          }
        }
      }
    }
    stage('Cleaning up') {
      agent any
      steps {
        sh 'docker rmi $registry:$BUILD_NUMBER'
        sh 'docker rmi $registry:latest'
      }
    }
  }
}
