
def call() {

    withMaven(jdk: 'Current JDK 8',
            maven: 'Current Maven 3',
            mavenLocalRepo: '${JENKINS_HOME}/maven-repositories/${EXECUTOR_NUMBER}/',
            globalMavenSettingsConfig: '9a4daf6d-06dd-434a-83cc-9ba9bd2326fc',
            options: [artifactsPublisher(disabled: true),
                      junitPublisher(disabled: true, ignoreAttachments: false)]) {
        sh "mvn -Dmaven.test.failure.ignore=true clean install"
        step([$class: 'Publisher'])
        jacoco exclusionPattern: '**/jaxb/*.class', execPattern: '**/jacoco.exec'
        jacoco exclusionPattern: '**/jaxb/*.class', execPattern: '**/jacoco-it.exec'
    }
}
