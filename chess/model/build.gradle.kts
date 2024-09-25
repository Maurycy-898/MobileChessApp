plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.mychessapp.chess.model"

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

    implementation(projects.core.common)
    implementation(libs.androidx.core.ktx)
}