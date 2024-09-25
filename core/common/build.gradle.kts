plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.mychessapp.core.common"

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

    implementation(libs.androidx.appcompat)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.bundles.kotest)
    testImplementation(libs.bundles.test.kotlin)
}
