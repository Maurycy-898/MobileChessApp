package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

class CommonAndroidModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
    }

  private fun applyPlugins(project: Project) {
  }
}

val Project.libs
  get(): VersionCatalog = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")