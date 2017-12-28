
def call() {
    script {
        if (currentBuild.result == null || currentBuild.result == 'SUCCESS') {
            if (currentBuild.previousBuild != null && currentBuild.previousBuild.result != 'SUCCESS') {
                emailext(
                        to: 'ci@sw4j.org',
                        recipientProviders: [[$class: 'CulpritsRecipientProvider'],
                                             [$class: 'DevelopersRecipientProvider'],
                                             [$class: 'FailingTestSuspectsRecipientProvider'],
                                             [$class: 'FirstFailingBuildSuspectsRecipientProvider']],
                        replyTo: 'ci@sw4j.org',
                        subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - is successful again',
                        body: '$PROJECT_NAME - Build # $BUILD_NUMBER - is successful again',
                        mimeType: 'text/plain')
            }
        } else {
            emailext(
                    to: 'ci@sw4j.org',
                    recipientProviders: [[$class: 'CulpritsRecipientProvider'],
                                         [$class: 'DevelopersRecipientProvider'],
                                         [$class: 'FailingTestSuspectsRecipientProvider'],
                                         [$class: 'FirstFailingBuildSuspectsRecipientProvider']],
                    replyTo: 'ci@sw4j.org',
                    subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - failed!',
                    body: '$PROJECT_NAME - Build # $BUILD_NUMBER - failed with state $BUILD_STATUS!\n' +
                            '\n' +
                            'Check console output at $BUILD_URL to view the results.',
                    mimeType: 'text/plain',
                    attachLog: true,
                    compressLog: true)
        }
    }
}