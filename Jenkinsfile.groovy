pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install -PbuildDocker'
            }
        }
    }
    post {
        always {
         }
     }
}
