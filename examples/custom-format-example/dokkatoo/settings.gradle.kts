rootProject.name = "custom-format-example"

pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
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
