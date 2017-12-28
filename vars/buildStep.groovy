
def call(body) {
    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    node() {
        withMaven(jdk: 'Current JDK 8',
                maven: 'Current Maven 3',
                mavenLocalRepo: '${JENKINS_HOME}/maven-repositories/${EXECUTOR_NUMBER}/',
                globalMavenSettingsConfig: '9a4daf6d-06dd-434a-83cc-9ba9bd2326fc',
                options: [artifactsPublisher(disabled: true),
                          junitPublisher(disabled: true, ignoreAttachments: false)]) {
            sh "mvn -Dmaven.test.failure.ignore=true clean install"
            step([$class: 'Publisher'])
        }
    }
    if (${config.doJacoco}) {
        jacoco exclusionPattern: '**/jaxb/*.class'
    }
    if (${config.doCheckstyle}) {
        withMaven(jdk: 'Current JDK 8',
                maven: 'Current Maven 3',
                mavenLocalRepo: '${JENKINS_HOME}/maven-repositories/${EXECUTOR_NUMBER}/',
                globalMavenSettingsConfig: '9a4daf6d-06dd-434a-83cc-9ba9bd2326fc') {
            sh "mvn checkstyle:checkstyle"
            checkstyle canComputeNew: false, pattern: '**/checkstyle-result.xml'
        }
    }
    if (${config.doSpotbugs}) {
        withMaven(jdk: 'Current JDK 8',
                maven: 'Current Maven 3',
                mavenLocalRepo: '${JENKINS_HOME}/maven-repositories/${EXECUTOR_NUMBER}/',
                globalMavenSettingsConfig: '9a4daf6d-06dd-434a-83cc-9ba9bd2326fc') {
            sh "mvn findbugs:findbugs"
            findbugs canComputeNew: false, pattern: '**/target/findbugsXml.xml'
        }
    }
    if (${config.doPmd}) {
        withMaven(jdk: 'Current JDK 8',
                maven: 'Current Maven 3',
                mavenLocalRepo: '${JENKINS_HOME}/maven-repositories/${EXECUTOR_NUMBER}/',
                globalMavenSettingsConfig: '9a4daf6d-06dd-434a-83cc-9ba9bd2326fc') {
            sh "mvn pmd:pmd"
            pmd canComputeNew: false, pattern: '**/pmd.xml'
        }
    }
}
