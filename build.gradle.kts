buildscript {
    repositories {
        google()  // ✅ Required for `com.android.library`
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.1")  // ✅ Ensure the Android Gradle Plugin is available
    }
}


plugins {
    id("com.android.library")  // ✅ Use full plugin ID
    id("org.jetbrains.kotlin.android")  // ✅ Use full plugin ID
    id("maven-publish")

}


android {
    namespace = "com.ijktech.bytegpt"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        version = 1

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("com.microsoft.onnxruntime:onnxruntime-android:1.16.3")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.ijktech"
                artifactId = "ByteGPT-Android"
                version = "1.0.4"
            }
        }
    }
}