pipeline {
    agent any
    stages {
        stage('Check Environment Variables') {
            steps {
                script {
                    // Imprime todas las variables de entorno
                    sh 'env'

                    // O si prefieres filtrar específicamente por Docker
                    // sh 'env | grep DOCKER'
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install -PbuildDocker'
            }
        }
    }
}
