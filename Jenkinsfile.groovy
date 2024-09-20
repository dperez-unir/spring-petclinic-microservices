pipeline {
    agent any
    stages {

        stage('Update Maven Versions') {
            steps {
                script {
                    sh '''
                        git config --global user.email "you@example.com"
                        git config --global user.name "Tu Nombre"
                        currentVersion=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout) && \
                        newVersion=$(echo $currentVersion | awk -F "." \'{print $1"."$2"."$3+1}\') && \
                        find . -name "pom.xml" -exec sed -i "s/<version>$currentVersion<\\/version>/<version>$newVersion<\\/version>/" {} +
                        git commit -am "Subida de versión"
                    '''
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
