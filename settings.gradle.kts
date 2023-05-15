rootProject.name = "dokkatoo"

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {

  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

  repositories {
    mavenCentral()
    google()

    maven("https://www.jetbrains.com/intellij-repository/snapshots")
    maven("https://www.jetbrains.com/intellij-repository/releases")
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-ide")
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-ide-plugin-dependencies")
    maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
    maven("https://www.myget.org/F/rd-snapshots/maven/")

    ivy("https://github.com/") {
      name = "GitHub Release"
      patternLayout {
        artifact("[organization]/[module]/archive/[revision].[ext]")
        artifact("[organization]/[module]/archive/refs/tags/[revision].[ext]")
        artifact("[organization]/[module]/archive/refs/tags/v[revision].[ext]")
      }
      metadataSources { artifact() }
    }
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

include(
  ":docs",
  ":examples",

  ":modules:dokkatoo-plugin",
  ":modules:dokkatoo-plugin-integration-tests",
)


enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")


//if (file("./examples/build/tmp/prepareDokkaSource").exists()) {
//  includeBuild("./examples/build/tmp/prepareDokkaSource")
//}

listOf(
  "examples",
  "modules/dokkatoo-plugin-integration-tests/projects",
).forEach { exampleProjectDir ->
  file(exampleProjectDir)
    .walk()
    .filter {
      it.isDirectory
          && it.name == "dokkatoo"
          && (
          it.resolve("settings.gradle.kts").exists()
              ||
              it.resolve("settings.gradle").exists()
          )
    }.forEach { file ->
      includeBuild(file) {
        name = file.parentFile.name
        println("$file $name")
      }
    }
}
