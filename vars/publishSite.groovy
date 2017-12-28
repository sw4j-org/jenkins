
def call() {
    sshagent(['4cab8b17-578f-49fc-908b-0e318625d63b']) {
        withMaven(jdk: 'Current JDK 8',
                maven: 'Current Maven 3',
                mavenLocalRepo: '${JENKINS_HOME}/maven-repositories/${EXECUTOR_NUMBER}/',
                globalMavenSettingsConfig: '03c863c2-c19c-4ed5-bc3a-7650b8f73ecf') {
            sh "mvn post-site scm-publish:publish-scm"
        }
    }
}
