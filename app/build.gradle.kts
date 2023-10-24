plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.firebaseecom"
    compileSdk = 34

    dataBinding{
        enable = true
    }

    defaultConfig {
        applicationId = "com.example.firebaseecom"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-fido:20.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")//viewpager
    implementation ("com.github.bumptech.glide:glide:4.12.0")//glide
    kapt("com.github.bumptech.glide:compiler:4.12.0")
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))//firebase
    implementation ("com.google.android.material:material:1.2.0-alpha05")//NavMenu
    implementation("com.google.firebase:firebase-firestore:24.9.0")//cloudFirestore

    implementation ("com.google.dagger:hilt-android:2.44")//hilt
    kapt ("com.google.dagger:hilt-android-compiler:2.44")
}