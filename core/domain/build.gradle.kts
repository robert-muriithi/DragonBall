plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.robert.domain"
    compileSdk = 36
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.common)
    implementation(libs.kotlinx.collections.immutable)


    // I added this for @Inject annotation only (no Hilt dependency needed in domain)
    implementation(libs.javax.inject)

    testImplementation(libs.junit)
}