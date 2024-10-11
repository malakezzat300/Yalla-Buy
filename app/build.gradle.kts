plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.malakezzat.yallabuy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.malakezzat.yallabuy"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.espresso.core)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.location)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Glide ->  Doesn't supported from Jetpack compose (use Coil)
    implementation("io.coil-kt:coil-compose:2.0.0")

    //Retrofit
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    //Room
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    //Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    //okhttp3
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-compose-android:2.8.6")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.8.1")

    implementation("androidx.compose.material:material:1.3.1")
    implementation ("androidx.compose.material:material-icons-extended:1.0.0")

    //implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation ("androidx.compose.material3:material3:1.0.1")

    implementation ("com.google.firebase:firebase-auth:22.1.1")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")


    //lottie
    implementation ("com.airbnb.android:lottie:5.0.3")
    implementation ("com.airbnb.android:lottie-compose:5.0.3")

    //map
    implementation ("org.osmdroid:osmdroid-android:6.1.11")

    // InstantTaskExecutorRule
    // AndroidX and Robolectric
    testImplementation("androidx.test.ext:junit-ktx:1.1.3")
    testImplementation("androidx.test:core-ktx:1.4.0")
    testImplementation("org.robolectric:robolectric:4.8")

// AndroidX Test - JVM testing
    testImplementation("androidx.test:core-ktx:1.4.0")

// AndroidX Test - Instrumented testing
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

// Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

// Hamcrest
    testImplementation("org.hamcrest:hamcrest:2.2")
// testImplementation("org.hamcrest:hamcrest-all:1.3")
    //androidTestImplementation("org.hamcrest:hamcrest:2.2")

// Dependencies for local unit tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.robolectric:robolectric:4.5.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")

}