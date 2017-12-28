
def call() {

    withMaven(jdk: 'Current JDK 8',
            maven: 'Current Maven 3',
            mavenLocalRepo: '${JENKINS_HOME}/maven-repositories/${EXECUTOR_NUMBER}/',
            globalMavenSettingsConfig: '9a4daf6d-06dd-434a-83cc-9ba9bd2326fc') {
        sh "mvn pmd:pmd"
        pmd canComputeNew: false, pattern: '**/pmd.xml'
    }
}
