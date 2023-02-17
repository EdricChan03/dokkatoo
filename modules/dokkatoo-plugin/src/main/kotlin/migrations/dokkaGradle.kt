@file:Suppress("PackageDirectoryMismatch")

package org.jetbrains.dokka.gradle

import dev.adamko.dokkatoo.dokka.parameters.DokkaSourceSetGradleBuilder
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

// temp disabled - maybe move migrations to a separate subproject?

//import dev.adamko.dokkatoo.dokka.parameters.DokkaSourceSetGradleBuilder
//import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
//
////@Deprecated("dokka2")
////typealias DokkaTask = org.jetbrains.dokka.gradle.tasks.DokkaConfigurationTask
//
//
/**
 * Extension allowing configuration of Dokka source sets via Kotlin Gradle plugin source sets.
 */
@Deprecated("dokkatoo...") // TODO what's the replacement...?
fun DokkaSourceSetGradleBuilder.kotlinSourceSet(kotlinSourceSet: KotlinSourceSet) {
  todoSourceSetName.set(kotlinSourceSet.name)
}