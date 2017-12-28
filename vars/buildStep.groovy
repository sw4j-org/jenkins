
def call(boolean doCheckstyle = true,
         boolean doSpotbugs = true,
         boolean doJacoco = true,
         boolean doPmd = true) {
    withMaven(jdk: 'Current JDK 8',
            maven: 'Current Maven 3',
            mavenLocalRepo: '${JENKINS_HOME}/maven-repositories/${EXECUTOR_NUMBER}/',
            globalMavenSettingsConfig: '9a4daf6d-06dd-434a-83cc-9ba9bd2326fc',
            options: [artifactsPublisher(disabled: true),
                      junitPublisher(disabled: true, ignoreAttachments: false)]) {
        mvnCmd = "mvn -Dmaven.test.failure.ignore=true clean install"
        if (doCheckstyle) { mvnCmd += " checkstyle:checkstyle" }
        if (doSpotbugs) { mvnCmd += " findbugs:findbugs" }
        if (doPmd) { mvnCmd += " pmd:pmd" }
        sh mvnCmd
        step([$class: 'Publisher'])
        if (doCheckstyle) {
            checkstyle canComputeNew: false, pattern: '**/checkstyle-result.xml'
        }
        if (doSpotbugs) {
            findbugs canComputeNew: false, pattern: '**/target/findbugsXml.xml'
        }
        if (doJacoco) {
            jacoco exclusionPattern: '**/jaxb/*.class'
        }
        if (doPmd) {
            pmd canComputeNew: false, pattern: '**/pmd.xml'
        }
    }
}