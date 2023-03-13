plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.leoleo.kmmgithubsearch.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.leoleo.kmmgithubsearch.android"
        minSdk = 31
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // Kotlin Versionとの互換性マップ
        // https://developer.android.com/jetpack/androidx/releases/compose-kotlin
        kotlinCompilerExtensionVersion = "1.4.2"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":shared"))

    // compose Bomを利用
    val composeBomVersion = platform("androidx.compose:compose-bom:2023.01.00")
    implementation(composeBomVersion)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class") // responsive UI for larger screens
    implementation("androidx.activity:activity-compose:1.6.1")

    // Compose: Navigation
    implementation("androidx.navigation:navigation-compose:2.5.3")
    // Image
    implementation("io.coil-kt:coil-compose:2.2.2")
    // Paging
    //implementation("androidx.paging:paging-compose:1.0.0-alpha18")
}