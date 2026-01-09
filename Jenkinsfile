pipeline {
    agent any

    environment {
        // Environment variables
        DB_CONTAINER_NAME = "car_rental_db"
        BACKEND_CONTAINER_NAME = "car_rental_backend"
        FRONTEND_CONTAINER_NAME = "car_rental_frontend"
        // Force CI to false to ignore CRA warnings
        CI = 'false'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code from Git...'
                git branch: 'main', url: 'https://github.com/akadirdgn/Car-Rental-Full-Stack-Project.git' // Replace with your repo
            }
        }

        stage('Build') {
            steps {
                script {
                    echo 'Building Backend...'
                    dir('car-rental-system-backend') {
                         bat 'mvn clean package -DskipTests'
                    }
                    
                    echo 'Building Frontend...'
                    dir('car-rental-system-frontend') {
                         bat 'npm install'
                         bat 'npm run build'
                    }
                }
            }
        }

        stage('Unit Tests') {
            steps {
                dir('car-rental-system-backend') {
                    echo 'Running Backend Unit Tests...'
                    bat 'mvn test'
                }
            }
            post {
                always {
                    junit 'car-rental-system-backend/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Integration Tests') {
             steps {
                dir('car-rental-system-backend') {
                    echo 'Running Backend Integration Tests...'

                     bat 'mvn verify -DskipUnitTests' 
                }
            }
        }

        stage('Docker Deploy') {
            steps {
                echo 'Deploying to Docker...'
                // Ensure docker-compose is installed
                // Force remove any existing containers with these names (even if not part of this compose project)
                // Using "|| exit 0" to suppress error if containers don't exist
                bat 'docker rm -f car_rental_db car_rental_backend car_rental_frontend || exit 0'
                bat 'docker-compose down --volumes'
                bat 'docker-compose up -d --build'
                
                // Wait for services to be ready
                sleep time: 60, unit: 'SECONDS'
            }
        }

        stage('E2E: UI Smoke Tests') {
            steps {
                dir('automation-tests') {
                    echo 'Running Common UI Tests...'
                    bat 'mvn test -Dtest=CommonTests'
                }
            }
             post {
                always {
                     junit 'automation-tests/target/surefire-reports/*.xml'
                }
            }
        }

        stage('E2E: Auth Boundary Tests') {
            steps {
                dir('automation-tests') {
                    echo 'Running Auth Edge Cases...'
                    bat 'mvn test -Dtest=AuthMoreTest'
                }
            }
             post {
                always {
                     junit 'automation-tests/target/surefire-reports/*.xml'
                }
            }
        }

        stage('E2E: Full Auth Flows') {
            steps {
                dir('automation-tests') {
                    echo 'Running Critical Auth Flows...'
                    bat 'mvn test -Dtest=AuthTest'
                }
            }
            post {
                always {
                     junit 'automation-tests/target/surefire-reports/*.xml'
                }
            }
        }

    post {
        always {
            echo 'Pipeline Finished.'
        }
        failure {
            echo 'Pipeline Failed!'
        }
    }
}
