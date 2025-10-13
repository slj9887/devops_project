pipeline {
  agent any
  stages {
    stage('Checkout & List') {
      steps {
        sh 'pwd && git rev-parse --short HEAD || true'
        sh 'ls -al'
        echo '✅ Jenkins can read the repo.'
      }
    }
  }
}
