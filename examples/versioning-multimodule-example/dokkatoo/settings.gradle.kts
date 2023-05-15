rootProject.name = "versioning-multimodule-example"

pluginManagement {
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
