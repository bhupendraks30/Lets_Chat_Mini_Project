plugins {
    id("com.android.application")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.letschat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.letschat"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-firestore:24.4.1")
    implementation("com.google.firebase:firebase-database:20.1.0")
    implementation("com.google.firebase:firebase-messaging:23.3.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation("junit:junit:4.13.2")
    implementation("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation ("com.hbb20:ccp:2.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.karumi:dexter:6.2.3")
    implementation ("com.github.bumptech.glide:glide:4.16.0")

}