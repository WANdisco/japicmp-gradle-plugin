package me.champeau.gradle

import org.gradle.testkit.runner.TaskOutcome

class SourceIncompatibleChangeWithIgnoreFile extends BaseFunctionalTest {
    String testProject = "source-incompatible-change-with-ignore-file"

    def "source incompatibility"() {
        when:
        def result = fails "japiTest"

        then:
        result.task(":japiTest").outcome == TaskOutcome.FAILED
    }

    def "source incompatibility with ignore file"() {
        when:
        def result = run "japiTestWithIgnore"

        then:
        result.task(":japiTestWithIgnore").outcome == TaskOutcome.SUCCESS
    }
}
