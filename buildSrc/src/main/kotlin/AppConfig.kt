import org.gradle.api.JavaVersion
import java.time.Instant

object AppConfig {
    const val minSdk = 26
    const val compileSdk = 34
    const val targetSdk = 34

    const val applicationId = "com.mychessapp.android"
    val versionCode = generateVersionCode()

    const val jvmTarget = "17"
    val sourceCompatibility = JavaVersion.VERSION_17
    val targetCompatibility = JavaVersion.VERSION_17
}

private fun generateVersionCode() =
    Instant.now().epochSecond.toInt()
