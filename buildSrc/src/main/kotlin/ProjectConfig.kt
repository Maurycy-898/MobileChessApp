import org.gradle.api.JavaVersion
import java.time.Instant

object ProjectConfig {
  const val minSdk = 31
  const val compileSdk = 35
  const val targetSdk = 35

  const val jvmTarget = "17"
  const val applicationId = "com.mychessapp.android"

  val versionCode = generateVersionCode()
  val sourceCompatibility = JavaVersion.VERSION_17
  val targetCompatibility = JavaVersion.VERSION_17
}

private fun generateVersionCode(): Int =
  Instant.now()
    .epochSecond
    .toInt()
