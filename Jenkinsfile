pipeline {
    agent any

    tools {
     jdk 'jdk 17'
     maven 'maven3'
    }

    environment{
        SCANNER_HOHE= tool 'sonar-scanner'
    }
    stages {
        stage('Git Checkout') {
            steps {
                 git branch: 'master', url: 'https://github.com/Srinu-rj/mysql-application'
            }
        }

        stage('Integration Test') {
            steps {
                sh "mvn clean install -DskipTests=true"
            }
        }

        stage('Maven Compile') {
            steps {
                echo "Maven Compile started..."
                sh "mvn compile"
            }
        }

        stage('SonarQube-Analysis') {
            steps {
                script {
                 echo "sonarqube code analysis"
                 withSonarQubeEnv(credentialsId: 'sonar-token') {
                     sh ''' $SCANNER_HOHE/bin/sonar-scanner -Dsonar.projectName=spring-application-with-  -Dsonar.projectKey=spring-application-with-mysql \
                     -Dsonar.java.binaries=. '''
                     echo "End of sonarqube code analysis"

                   }
                }
            }
        }
        stage('Docker Images') {
            steps {
                script {
                 echo "Docker Image started..."
                 withDockerRegistry(credentialsId: 'docker-cred', toolName: 'docker') {
                    sh "docker build -t ncpl-devops-one ."
                 }
                 echo "End of Docker Images"
                }
            }
        }

        stage("Tag & Push to DockerHub"){
            steps{
                script {
                    echo "Tag & Push to DockerHub Started..."
                    withDockerRegistry(credentialsId: 'docker-cred', toolName: 'docker') {
                      sh "docker tag ncpl-devops-one srinu641/ncpl-devops-one:V3.001"
                      sh "docker push srinu641/ncpl-devops-one:V3.001"
                    }
                    echo "End of Tag & Push to DockerHub"
                }
            }
        }

        stage('Docker Image Scan') {
            steps {
                sh "trivy image --format table -o srinu641/ncpl-devops-one:V3.001"
            }
        }


    }
}