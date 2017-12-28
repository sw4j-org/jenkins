
def call() {
    withMaven(jdk: 'Current JDK 8',
            maven: 'Current Maven 3',
            mavenLocalRepo: '${JENKINS_HOME}/maven-repositories/${EXECUTOR_NUMBER}/',
            options: [artifactsPublisher(disabled: true),
                      junitPublisher(disabled: true, ignoreAttachments: false)]) {
        sh "mvn -Dmaven.test.failure.ignore=true clean install"
        step([$class: 'Publisher'])
    }
}