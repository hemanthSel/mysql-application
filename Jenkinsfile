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

        // clean removes the target/ directory (compiled code and artifacts).
        stage('Integration Test') {
            steps {
                sh "mvn clean install -DskipTests=true"
            }
        }

        // mvn compile runs the Maven compile phase, which compiles the project's source code.
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

//         // abortPipeline: false means the pipeline will not stop even if the quality gate fails.
//         stage('Quality Gate') {
//             steps {
//                 script {
//                   echo "sonarqube Quality Gate"
//                   waitForQualityGate abortPipeline: false, credentialsId: 'sonar-token'
//                   echo "End Sonarqube Quality Gate"
//
//                 }
//             }
//         }

        // Compiles the code and packages it into a JAR/WAR file inside the target/ directory.
        stage('Mvn Build') {
            steps {
              sh "mvn package"
            }
        }

        // Authenticates with DockerHub (or another registry) using credentials stored in Jenkins (docker-cred).
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
                 sh "trivy image --format table -o trivy-image-report.html srinu641/ncpl-devops-one:V3.001"
                 sh "trivy clean --java-db"
            }
        }


    }
}