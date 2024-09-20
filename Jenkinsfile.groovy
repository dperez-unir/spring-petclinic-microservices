pipeline {
    agent any
    stages {
        stage('Check Environment Variables') {
            steps {
                script {
                    // Imprime todas las variables de entorno
                    sh 'env'
                }
            }
        }

        stage('Update Maven Versions') {
            steps {
                script {
                    // Usa el plugin de Maven Versions para cambiar la versión de todos los poms
                    sh 'mvn versions:set -DnewVersion=`mvn help:evaluate -Dexpression=project.version | grep -v "\\[INFO\\]" | awk -F \'.\' \'{print $1"."$2"."$3+1}\'`'

                    // Commit los cambios con un mensaje de commit adecuado
                    sh 'git commit -am "Subida de versión"'
                }
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install -PbuildDocker'
            }
        }

        stage('Push Changes') {
            steps {
                script {
                    // Empuja los cambios al repositorio
                    sh 'git push origin HEAD'
                }
            }
        }
    }
}
