plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

apply(plugin = "kotlin-kapt")

android {
    namespace = "com.example.emptyactivity"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.emptyactivity"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.put("room.schemaLocation", "$projectDir/schemas")
            }
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }  // Make sure JitPack is here too
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.play.services.cast.framework)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.imagepicker)

    implementation("androidx.room:room-runtime:2.6.1")  // Core Room Library
    implementation("androidx.room:room-ktx:2.6.1")      // Kotlin Extensions for Room
    implementation ("com.google.android.material:material:1.4.0")
    implementation("at.favre.lib:bcrypt:0.9.0")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.test:core:1.4.0")
    implementation("androidx.test.ext:junit:1.1.3")
    implementation("androidx.test:runner:1.4.0")

    // Room Database testing support
    androidTestImplementation("androidx.room:room-testing:2.4.0")


}