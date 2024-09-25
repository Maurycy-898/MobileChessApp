plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.mychessapp.chess.client"

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
    implementation(libs.androidx.core.ktx)
    coreLibraryDesugaring(libs.android.desugar.jdk)
}
