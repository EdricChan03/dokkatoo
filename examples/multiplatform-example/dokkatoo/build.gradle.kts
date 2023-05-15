plugins {
  kotlin("multiplatform") version "1.8.21"
  id("dev.adamko.dokkatoo") version "1.4.0-SNAPSHOT"
  id("com.android.library") version "7.4.0" // IJ 2023.1 supports up to 7.4.0
}

group = "org.dokka.example"
version = "1.0-SNAPSHOT"

kotlin {
  jvm() // Creates a JVM target with the default name "jvm"
  linuxX64("linux")
  macosX64("macos")
  js(BOTH) {
    browser()
  }
  android()

  sourceSets {
    commonMain {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
      }
    }

    val androidMain by getting {
      dependencies {
        implementation("androidx.appcompat:appcompat:1.6.1")
        
        // Compose
        implementation(platform("androidx.compose:compose-bom:2023.04.01"))
        implementation("androidx.compose.material3:material3")
      }
    }
  }
}

@Suppress("UnstableApiUsage")
android {
  compileSdk = 33
  namespace = "org.kotlintestmpp"

  compileOptions { 
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = "1.4.7"
}

dokkatoo {
  // Create a custom source set not known to the Kotlin Gradle Plugin
  dokkatooSourceSets.register("customSourceSet") {
    jdkVersion.set(9)
    displayName.set("custom")
    sourceRoots.from("src/customJdk9/kotlin")
  }
}



dokkatoo {
  // DON'T COPY - this is only needed for internal Dokkatoo integration tests
  sourceSetScopeDefault.set( /* DON'T COPY */ ":dokkaHtml")
  versions.jetbrainsDokka.set( /* DON'T COPY */ "1.7.20")
}
