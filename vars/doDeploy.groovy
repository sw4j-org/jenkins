
def call() {
    withMaven(jdk: 'Current JDK 8',
            maven: 'Current Maven 3',
            mavenLocalRepo: '${JENKINS_HOME}/maven-repositories/${EXECUTOR_NUMBER}/',
            globalMavenSettingsConfig: '03c863c2-c19c-4ed5-bc3a-7650b8f73ecf') {
        sh "mvn deploy"
    }
}
