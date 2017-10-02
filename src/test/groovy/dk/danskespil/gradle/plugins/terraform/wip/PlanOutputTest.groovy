package dk.danskespil.gradle.plugins.terraform.wip

import dk.danskespil.gradle.plugins.helpers.DSSpecification
import org.gradle.testkit.runner.TaskOutcome

class PlanOutputTest extends DSSpecification {
// This is what we are coding against:
//    task plan {
//        inputs.files fileTree("$projectDir").include('*.tf')
//        inputs.files fileTree("$projectDir").include('*.tpl')
//        inputs.files get.outputs.files
//        inputs.files remoteConfig.outputs.files
//
//        outputs.files file("${projectDir}/plan-output.bin")
//        outputs.files file("${projectDir}/plan-output")
//
//        dependsOn validate, ":docker-certificates:build"
//
//        doLast {
//            TerraformStatic.plan(project)
//        }
//    }

    def "Can save output from plan in a text file"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.Plan) {
             outAsText=file('plan-output')
          }
        """

        when:
        def result = buildWithTasks('cut')

        then:
        result
        result.task(':cut').outcome == TaskOutcome.SUCCESS
        new File(testProjectDir.root.getAbsolutePath() + "/plan-output").exists()
    }

    def "When saving output from plan to a text file, its also echoed to stdout, so the user can see it"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.Plan) {
             outAsText=file('plan-output')
          }
        """

        when:
        def result = buildWithTasks('cut')

        then:
        result
        result.task(':cut').outcome == TaskOutcome.SUCCESS
        result.output.contains('terraform plan')
    }

    def "When saving output from plan to a text file, that text file contains the expected output"() {
        given:
        buildFile << """
          plugins {
              id 'dk.danskespil.gradle.plugins.terraform'
          }

          task cut(type: dk.danskespil.gradle.plugins.terraform.Plan) {
             outAsText=file('plan-output')
          }
        """

        when:
        def result = buildWithTasks('cut')

        then:
        result
        result.task(':cut').outcome == TaskOutcome.SUCCESS
        new File(testProjectDir.root.getAbsolutePath() + "/plan-output").exists()
        new File(testProjectDir.root.getAbsolutePath() + "/plan-output").text.contains('terraform plan')
    }
}