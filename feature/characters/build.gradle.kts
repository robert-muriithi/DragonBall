plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.robert.characters"
    compileSdk = 36
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
}

dependencies {
    implementation(libs.bundles.lifecycle.compose)
    implementation(libs.androidx.paging.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.ui)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.coil.compose)
    implementation(libs.androidx.palette)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Modules
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))

    testImplementation(libs.bundles.test.unit)
    debugImplementation(libs.androidx.compose.ui.tooling)
}