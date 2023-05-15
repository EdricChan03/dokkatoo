package buildsrc.conventions

import buildsrc.conventions.Maven_publish_test_gradle.MavenPublishTest
import buildsrc.tasks.SetupDokkaProjects
import org.gradle.kotlin.dsl.support.serviceOf

plugins {
  id("buildsrc.conventions.base")
  id("buildsrc.conventions.dokka-source-downloader")
  id("buildsrc.conventions.maven-publish-test")
  id("buildsrc.conventions.dokkatoo-example-projects-base")
}

val TASK_GROUP = "dokkatoo examples"

val prepareDokkaSourceTask = tasks.named<Sync>("prepareDokkaSource")

val setupDokkaTemplateProjects by tasks.registering(SetupDokkaProjects::class) {
  group = TASK_GROUP

  dependsOn(prepareDokkaSourceTask)

  // complicated workaround for https://github.com/gradle/gradle/issues/23708
  val layout = serviceOf<ProjectLayout>()
  val providers = serviceOf<ProviderFactory>()

  val dokkaSrcDir = prepareDokkaSourceTask.flatMap {
    layout.dir(providers.provider {
      it.destinationDir
    })
  }
  dokkaSourceDir.set(dokkaSrcDir)

  destinationToSources.convention(emptyMap())
}

val mavenPublishTestExtension = extensions.getByType<MavenPublishTest>()


val updateDokkatooExamplesGradleProperties by tasks.registering {
  group = TASK_GROUP

  mustRunAfter(tasks.withType<SetupDokkaProjects>())

  val gradlePropertiesFiles =
    layout.projectDirectory.asFileTree
      .matching {
        include(
          "**/*dokkatoo*/settings.gradle.kts",
          "**/*dokkatoo*/settings.gradle",
        )
      }.elements.map { settingsFiles ->
        settingsFiles.map {
          it.asFile.resolveSibling("gradle.properties")
        }
      }

  outputs.files(gradlePropertiesFiles)

  val testMavenRepoPath = mavenPublishTestExtension.testMavenRepo.map {
    it.asFile.invariantSeparatorsPath
  }
  inputs.property("testMavenRepoPath", testMavenRepoPath)

  doLast task@{
    gradlePropertiesFiles.get().forEach {
      it.writeText(
        """
          |# DO NOT EDIT - Generated by ${this@task.path}
          |
          |testMavenRepo=${testMavenRepoPath.get()}
          |
        """.trimMargin()
      )
      println("$it: ${it.readText()}")
    }
  }
}

val dokkatooVersion = provider { project.version.toString() }

val updateDokkatooExamplesBuildFiles by tasks.registering {
  group = TASK_GROUP
  description = "Update the Gradle build files in the Dokkatoo examples"

  outputs.upToDateWhen { false }

  mustRunAfter(tasks.withType<SetupDokkaProjects>())
  shouldRunAfter(updateDokkatooExamplesGradleProperties)

  val dokkatooVersion = dokkatooVersion

  val dokkatooPluginVersionMatcher = """
    id[^"]+?\"dev\.adamko\.dokkatoo\".+?version \"([^"]+?)\"
    """.trimIndent().toRegex()

  val gradleBuildFiles =
    layout.projectDirectory.asFileTree
      .matching {
        include(
          "**/*dokkatoo*/**/build.gradle.kts",
          "**/*dokkatoo*/**/build.gradle",
        )
      }.elements

  outputs.files(gradleBuildFiles)

  doLast {
    gradleBuildFiles.get().forEach {
      val file = it.asFile
      if (file.exists()) {
        file.writeText(
          file.readText().replace(dokkatooPluginVersionMatcher) {
            val oldVersion = it.groupValues[1]
            it.value.replace(oldVersion, dokkatooVersion.get())
          })
      }
    }
  }
}


val updateDokkatooExamples by tasks.registering task@{
  group = TASK_GROUP
  description = "lifecycle task for all '$TASK_GROUP' tasks"
  dependsOn(
    setupDokkaTemplateProjects,
    updateDokkatooExamplesGradleProperties,
    updateDokkatooExamplesBuildFiles,
  )
}

tasks.assemble {
  dependsOn(updateDokkatooExamples)
}
