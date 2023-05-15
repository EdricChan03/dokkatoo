rootProject.name = "dokkatoo-multimodule-example"

pluginManagement {
  plugins {
    kotlin("jvm") version "1.7.20"
  }
  repositories {
    gradlePluginPortal()
    mavenCentral()
    providers.gradleProperty("testMavenRepo").takeIf { it.isPresent }?.let { maven(it) }
  }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  repositories {
    mavenCentral()
    providers.gradleProperty("testMavenRepo").takeIf { it.isPresent }?.let { maven(it) }
  }
}

include(":parentProject")
include(":parentProject:childProjectA")
include(":parentProject:childProjectB")
