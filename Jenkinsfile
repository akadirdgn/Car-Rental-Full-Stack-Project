pipeline {
    agent any

    environment {
        // Environment variables
        DB_CONTAINER_NAME = "car_rental_db"
        BACKEND_CONTAINER_NAME = "car_rental_backend"
        FRONTEND_CONTAINER_NAME = "car_rental_frontend"
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
                        // Assuming running on Windows agent or container with Maven
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
                    // Typically 'mvn verify' runs integration tests (failsafe plugin)
                    // Ensuring we run the UserResourceTest we created
                     bat 'mvn verify -DskipUnitTests' 
                }
            }
        }

        stage('Docker Deploy') {
            steps {
                echo 'Deploying to Docker...'
                // Ensure docker-compose is installed
                bat 'docker-compose down'
                bat 'docker-compose up -d --build'
                
                // Wait for services to be ready
                sleep time: 30, unit: 'SECONDS'
            }
        }

        stage('E2E Tests') {
            steps {
                dir('automation-tests') {
                    echo 'Running Selenium E2E Tests...'
                    // Run the TestNG suite
                    bat 'mvn test'
                }
            }
            post {
                always {
                     junit 'automation-tests/target/surefire-reports/*.xml'
                }
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
