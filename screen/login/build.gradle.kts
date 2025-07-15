import dependencies.implementation

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.dagger.hilt)
  alias(libs.plugins.jetbrains.kotlin.android)
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.mychessapp.screen.login"

  compileSdk = ProjectConfig.compileSdk

  defaultConfig {
    minSdk = ProjectConfig.minSdk
    lint.targetSdk = ProjectConfig.targetSdk
  }

  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = ProjectConfig.sourceCompatibility
    targetCompatibility = ProjectConfig.targetCompatibility
  }

  kotlinOptions {
    jvmTarget = ProjectConfig.jvmTarget
  }

  testOptions {
    unitTests.all(Test::useJUnitPlatform)
  }
}

dependencies {
  coreLibraryDesugaring(libs.android.desugar.jdk)

  implementation(projects.core.composeUi)
  implementation(projects.core.model)
  implementation(projects.core.strings)

  implementation(libs.bundles.androidx.compose)
  implementation(libs.androidx.core.ktx)
  implementation(libs.coil.compose)
  implementation(libs.bundles.dagger)

  debugImplementation(libs.androidx.compose.ui.tooling)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.junit)
  androidTestImplementation(libs.androidx.test.espresso.core)

  ksp(libs.dagger.compiler)
  ksp(libs.dagger.hilt.android.compiler)
}
