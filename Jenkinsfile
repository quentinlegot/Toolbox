pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                gradle build
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
                gradle test
            }
        }
    }
}