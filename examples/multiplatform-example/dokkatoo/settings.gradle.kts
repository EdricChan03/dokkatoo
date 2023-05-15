rootProject.name = "dokka-multiplatform-example"

pluginManagement {
  repositories {
    gradlePluginPortal()
    google() // Needed for AGP
    mavenCentral()
    providers.gradleProperty("testMavenRepo").takeIf { it.isPresent }?.let { maven(it) }
  }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google() // Android dependencies
    providers.gradleProperty("testMavenRepo").takeIf { it.isPresent }?.let { maven(it) }
  }
}
