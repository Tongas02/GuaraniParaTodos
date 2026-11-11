plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.helloworld.guaranparatodos"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.helloworld.guaranparatodos"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    dependencies {
        implementation("com.google.firebase:firebase-auth:24.2.0")
        implementation(libs.activity.ktx)
        implementation(libs.appcompat)
        implementation(libs.constraintlayout)
        implementation(libs.material)

        testImplementation(libs.junit)
        androidTestImplementation(libs.espresso.core)
        androidTestImplementation(libs.ext.junit)

        // ROOM
        val roomVersion = "2.8.5"

        implementation("androidx.room:room-runtime:$roomVersion")
        annotationProcessor("androidx.room:room-compiler:$roomVersion")
        testImplementation("androidx.room:room-testing:$roomVersion")
    }
}