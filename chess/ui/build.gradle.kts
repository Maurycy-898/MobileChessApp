plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.mychessapp.chess.ui"

    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        lint.targetSdk = AppConfig.targetSdk
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = AppConfig.sourceCompatibility
        targetCompatibility = AppConfig.targetCompatibility
    }

    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
    }

    testOptions {
        unitTests.all(Test::useJUnitPlatform)
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugar.jdk)

    implementation(projects.chess.model)
    implementation(projects.core.common)

    implementation(libs.bundles.androidx.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.compose)

    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
