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
      sh '''#!/bin/bash
      set -euo pipefail
      rm -rf "$WORKSPACE/.m2/repository/org/codehaus/plexus/plexus-utils"
      mvn -B -DskipTests \
        -Dmaven.wagon.http.retryHandler.count=3 \
        -Dhttps.protocols=TLSv1.2 \
        --no-transfer-progress \
        clean package spring-boot:repackage
      '''
    }
  }


    stage('Verify JAR') {
      steps {
        sh '''
        echo "== target 내용 ==" && ls -al target || true
        echo
        echo "== JAR 탐색 ==" && find target -maxdepth 1 -type f -name "*.jar" -print || true
        '''
      }
    }

    // ✅ Harbor 로그인: withCredentials + password-stdin + Groovy 보간 금지
    stage('Registry Login') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: "${DOCKER_CREDENTIAL_ID}",
          usernameVariable: 'REG_USER',
          passwordVariable: 'REG_PASS'
        )]) {
          sh '''#!/bin/bash
          set -euo pipefail
          echo "$REG_PASS" | docker login amdp-registry.skala-ai.com -u "$REG_USER" --password-stdin
          '''
        }
      }
    }

    stage('Build Docker image') {
      steps {
        script {
          env.IMAGE_URI = "${IMAGE_REGISTRY_URL}/${IMAGE_REGISTRY_PROJECT}/${IMAGE_NAME}:${IMAGE_TAG}"
          sh '''#!/bin/bash
          set -euo pipefail
          docker build -t "$IMAGE_URI" .
          '''
        }
      }
    }

    stage('Push Docker image') {
      steps {
        // ❌ (기존) """ ... ${REG_PASS} ... """ → 경고 발생
        // ✅ (수정) 셸에서 변수 확장: 비밀 노출 경고 X
        sh '''#!/bin/bash
        set -euo pipefail
        docker push "$IMAGE_URI"
        docker logout "${IMAGE_REGISTRY_URL}"
        '''
      }
    }
  }

  post {
    always {
      echo '✅ Pipeline Finished'
    }
  }
}
