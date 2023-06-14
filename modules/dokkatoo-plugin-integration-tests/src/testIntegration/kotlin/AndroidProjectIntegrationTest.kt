package dev.adamko.dokkatoo.tests.integration

import dev.adamko.dokkatoo.utils.*
import io.kotest.core.annotation.EnabledIf
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.file.shouldBeAFile
import io.kotest.matchers.file.shouldHaveSameStructureAndContentAs
import io.kotest.matchers.file.shouldHaveSameStructureAs
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.io.File

/**
 * Integration test for the `it-android-0` project in Dokka
 *
 * Runs Dokka & Dokkatoo, and compares the resulting HTML site.
 */
@EnabledIf(NotWindowsCondition::class) // https://github.com/adamko-dev/dokkatoo/issues/10
class AndroidProjectIntegrationTest : FunSpec({

  val tempDir = GradleProjectTest.projectTestTempDir.resolve("it/it-android-0").toFile()

  val dokkatooProject = initDokkatooProject(tempDir.resolve("dokkatoo"))
  val dokkaProject = initDokkaProject(tempDir.resolve("dokka"))

  context("when generating HTML") {
    val dokkaBuild = dokkaProject.runner
      .addArguments(
        "clean",
        "dokkaHtml",
        "--stacktrace",
      )
      .forwardOutput()
      .build()

    val dokkatooBuild = dokkatooProject.runner
      .addArguments(
        "clean",
        "dokkatooGeneratePublicationHtml",
        "--stacktrace",
      )
      .forwardOutput()
      .build()

    test("expect project builds successfully") {
      dokkatooBuild.output shouldContain "BUILD SUCCESSFUL"
    }

    context("with Dokka") {

      test("expect project builds successfully") {
        dokkaBuild.output shouldContain "BUILD SUCCESSFUL"
      }

      test("expect all dokka workers are successful") {

        val dokkaWorkerLogs = dokkatooProject.findFiles { it.name == "dokka-worker.log" }
        dokkaWorkerLogs.firstOrNull().shouldNotBeNull().should { dokkaWorkerLog ->
          dokkaWorkerLog.shouldBeAFile()
          dokkaWorkerLog.readText().shouldNotContainAnyOf(
            "[ERROR]",
            "[WARN]",
          )
        }
      }
    }

    context("with Dokkatoo") {

      test("expect all dokka workers are successful") {

        val dokkaWorkerLogs = dokkatooProject.findFiles { it.name == "dokka-worker.log" }
        dokkaWorkerLogs.firstOrNull().shouldNotBeNull().should { dokkaWorkerLog ->
          dokkaWorkerLog.shouldBeAFile()
          dokkaWorkerLog.readText().shouldNotContainAnyOf(
            "[ERROR]",
            "[WARN]",
          )
        }
      }
    }

    test("expect the same HTML is generated") {

      val dokkaHtmlDir = dokkaProject.projectDir.resolve("build/dokka/html")
      val dokkatooHtmlDir = dokkatooProject.projectDir.resolve("build/dokka/html")

      val expectedFileTree = dokkaHtmlDir.toTreeString()
      val actualFileTree = dokkatooHtmlDir.toTreeString()
      println((actualFileTree to expectedFileTree).sideBySide())
      expectedFileTree shouldBe actualFileTree

      dokkatooHtmlDir.toFile().shouldHaveSameStructureAs(dokkaHtmlDir.toFile())
      dokkatooHtmlDir.toFile().shouldHaveSameStructureAndContentAs(dokkaHtmlDir.toFile())
    }

    test("Dokkatoo tasks should be cacheable") {
      dokkatooProject.runner
        .addArguments(
          "dokkatooGeneratePublicationHtml",
          "--stacktrace",
          "--build-cache",
        )
        .forwardOutput()
        .build().should { buildResult ->
          buildResult.output shouldContainAll listOf(
            "Task :dokkatooGeneratePublicationHtml UP-TO-DATE",
          )
        }
    }

    context("expect Dokkatoo is compatible with Gradle Configuration Cache") {
      dokkatooProject.file(".gradle/configuration-cache").toFile().deleteRecursively()
      dokkatooProject.file("build/reports/configuration-cache").toFile().deleteRecursively()

      val configCacheRunner =
        dokkatooProject.runner
          .addArguments(
            "clean",
            "dokkatooGeneratePublicationHtml",
            "--stacktrace",
            "--no-build-cache",
            "--configuration-cache",
          )
          .forwardOutput()

      test("first build should store the configuration cache") {
        configCacheRunner.build().should { buildResult ->
          buildResult.output shouldContain "BUILD SUCCESSFUL"
          buildResult.output shouldContain "0 problems were found storing the configuration cache"
        }
      }

      test("second build should reuse the configuration cache") {
        configCacheRunner.build().should { buildResult ->
          buildResult.output shouldContain "BUILD SUCCESSFUL"
          buildResult.output shouldContain "Configuration cache entry reused"
        }
      }
    }
  }
})

private fun initDokkaProject(
  destinationDir: File,
): GradleProjectTest {
  return GradleProjectTest(destinationDir.toPath()).apply {
    copyIntegrationTestProject("it-android-0/dokka")
  }
}

private fun initDokkatooProject(
  destinationDir: File,
): GradleProjectTest {
  return GradleProjectTest(destinationDir.toPath()).apply {
    copyIntegrationTestProject("it-android-0/dokkatoo")
  }
}