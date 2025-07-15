import dependencies.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.gms)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.mobile.chessapp.app"

    defaultConfig {
        compileSdk = ProjectConfig.compileSdk
        versionCode = ProjectConfig.versionCode
        applicationId = ProjectConfig.applicationId

        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk

        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = ProjectConfig.sourceCompatibility
        targetCompatibility = ProjectConfig.targetCompatibility
    }

    hilt {
        enableAggregatingTask = false
    }

    kotlinOptions {
        jvmTarget = ProjectConfig.jvmTarget

    }

    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugar.jdk)

    implementation(projects.chess.model)
    implementation(projects.chess.ui)
    implementation(projects.core.composeUi)
    
    implementation(projects.screen.home)
    implementation(projects.screen.login)
    implementation(projects.screen.game)
    implementation(projects.screen.archive)
    implementation(projects.screen.profile)
    implementation(projects.screen.settings)


    implementation(libs.android.material)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.dagger)
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(platform(libs.firebase.bom))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.junit)

    ksp(libs.dagger.compiler)
    ksp(libs.dagger.hilt.android.compiler)
}
