plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android") version "2.2.0"
    id("com.chaquo.python")
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0"
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            // On Apple silicon, you can omit x86_64.
            abiFilters += listOf("arm64-v8a", "x86_64")
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}
chaquopy {
    defaultConfig {
        pip {
            install("pyproj")
        }
    }
}
dependencies {

    implementation("com.google.code.gson:gson:2.10.1")
    implementation(libs.appcompat)
    implementation("com.github.MikeOrtiz:TouchImageView:1.4.1")
    implementation("io.github.2307vivek:seeker:1.2.2")
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.location)
    implementation(libs.core.ktx)
    implementation(libs.runtime.android)
    implementation(libs.ui.android)
    implementation(libs.foundation.android)
    implementation(libs.material3.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}