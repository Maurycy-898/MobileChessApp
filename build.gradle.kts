import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradle)
        classpath(libs.android.junit5)
        classpath(libs.appcenter.gradle)
        classpath(libs.cappuccino.gradle)
        classpath(libs.dagger.hilt.android.gradle)
        classpath(libs.firebase.crashlytics.gradle)
        classpath(libs.google.services)
        classpath(libs.gradle.versions)
        classpath(libs.kotlin.gradle)
        classpath(libs.ksp.gradle)
        classpath(libs.paperwork.gradle)
        classpath(libs.play.publisher.gradle)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

subprojects {
//    apply(plugin = "org.jetbrains.kotlin.jvm")
    project.run {
        name
            .takeIf { subprojects.isNullOrEmpty() }
            ?.let(::println)

    }
}

