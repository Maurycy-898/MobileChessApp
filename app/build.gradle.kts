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

  compileSdk = AppConfig.compileSdk

  defaultConfig {
    versionCode = AppConfig.versionCode
    applicationId = AppConfig.applicationId

    minSdk = AppConfig.minSdk
    targetSdk = AppConfig.targetSdk

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
    sourceCompatibility = AppConfig.sourceCompatibility
    targetCompatibility = AppConfig.targetCompatibility
  }

  java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
  }

  hilt {
    enableAggregatingTask = false
  }

  kotlinOptions {
    jvmTarget = AppConfig.jvmTarget
  }

  buildFeatures {
    compose = true
    viewBinding = true
  }
}

dependencies {
  coreLibraryDesugaring(libs.android.desugar.jdk)

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
  implementation(libs.dagger)
  implementation(libs.dagger.hilt.android)
  implementation(libs.firebase.analytics.ktx)
  implementation(libs.firebase.database.ktx)
  implementation(platform(libs.firebase.bom))

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.espresso.core)
  androidTestImplementation(libs.androidx.test.junit)

  ksp(libs.dagger.compiler)
  ksp(libs.dagger.hilt.android.compiler)
}

val sourcesJar by tasks.registering(Jar::class) {
  archiveClassifier.set("sources")
  from(android.sourceSets.getByName("main").java.srcDirs)
}

