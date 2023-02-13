pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                withGradle {
                    sh 'gradle build'
                }
                
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
                withGradle {
                    sh 'gradle test'
                }
            }
        }
    }
}
