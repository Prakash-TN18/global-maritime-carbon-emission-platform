pipeline {

    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        COMPOSE_PROJECT_NAME = 'gmcemp'
    }

    stages {

        stage('Environment Check') {
            steps {
                bat 'java -version'
                bat 'mvn -version'
                bat 'docker --version'
                bat 'docker compose version'
            }
        }

        stage('Maven Clean Build') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Docker Compose Validation') {
            steps {
                bat 'docker compose config'
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker compose build'
            }
        }

        stage('Deploy') {
            steps {
                bat 'docker compose up -d'
            }
        }

        stage('Container Status') {
            steps {
                bat 'docker compose ps'
            }
        }
    }

    post {

        success {
            echo 'GMCEMP CI/CD pipeline completed successfully.'
        }

        failure {
            echo 'GMCEMP CI/CD pipeline failed. Check the stage logs.'
            bat 'docker compose ps -a'
        }

        always {
            archiveArtifacts(
                artifacts: '**/target/*.jar',
                fingerprint: true,
                allowEmptyArchive: true
            )
        }
    }
}