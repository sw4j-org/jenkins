
def call(body) {
    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    withMaven(jdk: 'Current JDK 8',
            maven: 'Current Maven 3',
            mavenLocalRepo: '${JENKINS_HOME}/maven-repositories/${EXECUTOR_NUMBER}/',
            globalMavenSettingsConfig: '9a4daf6d-06dd-434a-83cc-9ba9bd2326fc',
            options: [artifactsPublisher(disabled: true),
                      junitPublisher(disabled: true, ignoreAttachments: false)]) {
        mvnCmd = "mvn -Dmaven.test.failure.ignore=true clean install"
        if (${config.doCheckstyle}) { mvnCmd += " checkstyle:checkstyle" }
        if (${config.doSpotbugs}) { mvnCmd += " findbugs:findbugs" }
        if (${config.doPmd}) { mvnCmd += " pmd:pmd" }
        sh mvnCmd
        step([$class: 'Publisher'])
        if (${config.doCheckstyle}) {
            checkstyle canComputeNew: false, pattern: '**/checkstyle-result.xml'
        }
        if (${config.doSpotbugs}) {
            findbugs canComputeNew: false, pattern: '**/target/findbugsXml.xml'
        }
        if (${config.doJacoco}) {
            jacoco exclusionPattern: '**/jaxb/*.class'
        }
        if (${config.doPmd}) {
            pmd canComputeNew: false, pattern: '**/pmd.xml'
        }
    }
}