plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.mychessapp.chess.logic"

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
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }

    testOptions {
        unitTests.all(Test::useJUnitPlatform)
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugar.jdk)

    implementation(projects.chess.model)
    implementation(projects.core.common)

    implementation(libs.androidx.core.ktx)
    implementation(libs.dagger)
    implementation(libs.dagger.hilt.android)

    ksp(libs.dagger.compiler)
    ksp(libs.dagger.hilt.android.compiler)
}