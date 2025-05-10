import com.android.build.api.dsl.AaptOptions
import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.testImplementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.apollographql.apollo)
}
apollo {
    service("service") {
        packageName.set("com.panwar2001.leetcoderatingpredictorai")
        schemaFile.set(file("src/main/graphql/com/panwar2001/leetcoderatingpredictorai/schema.graphqls"))
        srcDir("src/main/graphql/com/panwar2001/leetcoderatingpredictorai")
    }
    service("service_2"){
        packageName.set("com.panwar2001.prediction")
        schemaFile.set(file("src/main/graphql/com/panwar2001/prediction/schema.graphqls"))
        srcDir("src/main/graphql/com/panwar2001/prediction")
    }
}

android {
    namespace = "com.panwar2001.leetcoderatingpredictorai"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.panwar2001.leetcoderatingpredictorai"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 35
        versionCode = 1
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources= true
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    hilt {    enableAggregatingTask = true }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.coil.kt.coil.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
    //room
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)

    //missing class
    implementation(libs.error.prone.annotations)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
    // context provider
    testImplementation(libs.androidx.core)

    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //GraphQL
    implementation(libs.apollo.runtime)

    //lifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)

    //Retrofit
    // Retrofit core
    implementation(libs.retrofit)
    // JSON converter using Gson
    implementation(libs.converter.gson)
    // OkHttp (Optional: Logging Interceptor)
    implementation(libs.logging.interceptor) // or latest
    // Tensorflow lite
    implementation(libs.litert)
    implementation(libs.litert.support)
    implementation(libs.litert.metadata)
    implementation(libs.tensorflow.lite.select.tf.ops)
}