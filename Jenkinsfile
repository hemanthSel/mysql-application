pipeline {
    agent any

    tools {
        jdk 'jdk 17'
        maven 'maven3'
    }

    environment {
        SCANNER_HOHE = tool 'sonar-scanner'
        IMAGE_NAME = 'srinu641/ncpl-devops-one'
        IMAGE_TAG = 'V3.001'
        TRIVY_REPORT = 'trivy-report.json'
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
                    echo "SonarQube code analysis"
                    withSonarQubeEnv(credentialsId: 'sonar-token') {
                        sh ''' $SCANNER_HOHE/bin/sonar-scanner -Dsonar.projectName=spring-application-with-mysql \
                        -Dsonar.projectKey=spring-application-with-mysql -Dsonar.java.binaries=. '''
                        echo "End of SonarQube code analysis"
                    }
                }
            }
        }

        stage('Mvn Build') {
            steps {
                sh "mvn package"
            }
        }

        stage('Docker Images') {
            steps {
                script {
                    echo "Docker Image started..."
                    withDockerRegistry(credentialsId: 'docker-cred', toolName: 'docker') {
                        sh "docker build -t ncpl-devops-one ."
                        sh 'docker images'
                    }
                    echo "End of Docker Images"
                }
            }
        }

        stage("Tag & Push to DockerHub") {
            steps {
                script {
                    echo "Tag & Push to DockerHub Started..."
                    withDockerRegistry(credentialsId: 'docker-cred', toolName: 'docker') {
                        sh "docker tag ncpl-devops-one srinu641/ncpl-devops-one:V3.001"
                        sh "docker push srinu641/ncpl-devops-one:V3.001"
                        sh 'docker images'
                    }
                    echo "End of Tag & Push to DockerHub"
                }
            }
        }

        stage('Verify Trivy Installation') {
            steps {
                script {
                    def trivyInstalled = sh(script: 'trivy --version', returnStatus: true)
                    if (trivyInstalled != 0) {
                        error "Trivy is not installed on the Jenkins agent. Install it before running the job."
                    } else {
                        echo "Trivy is installed."
                    }
                }
            }
        }

        stage('Scan Docker Image') {
            steps {
                script {
                    echo "Scanning Docker image: ${IMAGE_NAME}:${IMAGE_TAG}"
                    def scanStatus = sh(script: """
                        trivy image --format json -o ${TRIVY_REPORT} ${IMAGE_NAME}:${IMAGE_TAG}
                    """, returnStatus: true)

                    if (scanStatus != 0) {
                        error "Trivy scan failed. Please check the Docker image or Trivy configuration."
                    } else {
                        echo "Trivy scan completed. Report generated: ${TRIVY_REPORT}"
                    }
                }
            }
        }

        stage('Analyze Scan Results') {
            steps {
                script {
                    def highVulns = sh(script: """
                        cat ${TRIVY_REPORT} | jq '[.Results[].Vulnerabilities[] | select(.Severity == "HIGH")] | length'
                    """, returnStdout: true).trim()

                    echo "High severity vulnerabilities found: ${highVulns}"

                    if (highVulns.toInteger() > 0) {
                        error "High severity vulnerabilities found: ${highVulns}. Failing the build."
                    } else {
                        echo "No high severity vulnerabilities found. Proceeding..."
                    }
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: "${TRIVY_REPORT}", fingerprint: true
        }
    }
}
