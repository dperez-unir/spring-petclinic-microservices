pipeline {
    agent any
    stages {
        stage('Check Last Commit Message') {
            steps {
                script {
                    // Obtiene el mensaje del último commit directamente desde Git
                    def lastCommitMessage = sh(
                            script: "git log -1 --pretty=%B",
                            returnStdout: true
                    ).trim()

                    echo "Último mensaje de commit: ${lastCommitMessage}"

                    // Verifica si contiene la frase que deseas ignorar
                    if (lastCommitMessage.contains("subida de version Jenkins")) {
                        currentBuild.result = 'ABORTED'
                        error("Build aborted due to Jenkins auto-commit.")
                    }
                }
            }
        }
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
                    withCredentials([usernamePassword(credentialsId: '1d65426d-86d5-4860-af62-6afbcbb50317', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                        sh 'git add .'
                        sh 'git commit -am "subida de version Jenkins"'
                        sh 'git config user.email "tu-email@ejemplo.com"'
                        sh 'git config user.name "Tu Nombre"'
                        sh 'git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/dperez-unir/spring-petclinic-microservices.git HEAD:main'
                    }

                }
            }
        }
    }
}
