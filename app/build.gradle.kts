plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.vkompose)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "org.jugregator.op1buddy"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.jugregator.op1buddy"
        minSdk = 24
        targetSdk = 35
        versionCode = 5
        versionName = "0.0.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {

        debug {
            manifestPlaceholders["crashlyticsCollectionEnabled"] = "false"
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders["crashlyticsCollectionEnabled"] = "true"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

vkompose {
    skippabilityCheck = true
    testTag {
        isCleanerEnabled = true
    }
}

dependencies {
    // AAR's and deps
    implementation(libs.fat32lib)
    implementation(files("../libs/libaums-release.aar"))
    implementation(files("../libs/javafs-release.aar"))
    implementation(libs.log4j)
    implementation(libs.android.logging.log4j)
    implementation(libs.java.fs)

    implementation(libs.coroutines)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.google.fonts)
    implementation(libs.androidx.documentfile)

    // Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.navigation.compose)
    implementation(libs.androidx.compose.animation)

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)

    implementation(libs.kotlinx.serialization)
    implementation(libs.androidx.profileinstaller)

    implementation(libs.kotlinx.collections.immutable)


    // Firebase stuff
    implementation(platform(libs.firebase.bom))
    // Add the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.google.firebase.crashlytics)
    implementation(libs.google.firebase.analytics)
    implementation(libs.androidx.material3.android)

    testImplementation(libs.junit)
    testImplementation(libs.koin.test.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    "baselineProfile"(project(":baselineprofile"))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
