pipeline {
  agent any

  environment {
    GIT_URL                = 'https://github.com/slj9887/devops_project.git'
    GIT_BRANCH             = 'main'
    GIT_ID                 = 'skala-github-id'

    IMAGE_NAME             = 'spring-boot-app'
    IMAGE_TAG              = '1.0.0'
    IMAGE_REGISTRY_URL     = 'amdp-registry.skala-ai.com'
    IMAGE_REGISTRY_PROJECT = 'library'
    DOCKER_CREDENTIAL_ID   = 'skala-image-registry-id'
  }

  stages {

    stage('Checkout') {
      steps {
        git branch: "${GIT_BRANCH}", url: "${GIT_URL}", credentialsId: "${GIT_ID}"
      }
    }

    stage('Build JAR') {
    agent {
        docker {
        image 'maven:3.9.9-eclipse-temurin-17'
        args '-v $WORKSPACE/.m2:/root/.m2'
        }
    }
    steps {
        sh 'mvn -B -DskipTests clean package'
    }
    }


    stage('Build Docker image') {
      steps {
        script {
          sh """
            docker build -t ${IMAGE_REGISTRY_URL}/${IMAGE_REGISTRY_PROJECT}/${IMAGE_NAME}:${IMAGE_TAG} .
          """
        }
      }
    }

    stage('Push Docker image') {
      steps {
        withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIAL_ID}", usernameVariable: 'REG_USER', passwordVariable: 'REG_PASS')]) {
          sh """
            echo "${REG_PASS}" | docker login ${IMAGE_REGISTRY_URL} -u "${REG_USER}" --password-stdin
            docker push ${IMAGE_REGISTRY_URL}/${IMAGE_REGISTRY_PROJECT}/${IMAGE_NAME}:${IMAGE_TAG}
            docker logout ${IMAGE_REGISTRY_URL}
          """
        }
      }
    }
  }

  post {
    always {
      echo '✅ Pipeline Finished'
    }
  }
}
