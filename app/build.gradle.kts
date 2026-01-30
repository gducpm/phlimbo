
plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.gducpm.phlimbo"
    compileSdk = 36

    signingConfigs {
        create("release") {
            val keystoreBase64 = System.getenv("SIGNING_KEY_STORE_BASE64")

            if (!keystoreBase64.isNullOrEmpty()) {
                val keystoreFile = file("${layout.buildDirectory.get()}/release.jks")
                keystoreFile.parentFile.mkdirs()
                keystoreFile.writeBytes(
                    org.apache.commons.codec.binary.Base64.decodeBase64(keystoreBase64)
                )
                storeFile = keystoreFile
                storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
            }
        }
    }

    defaultConfig {
        applicationId = "com.gducpm.phlimbo"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}