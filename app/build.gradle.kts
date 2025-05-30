import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.apollographql.apollo").version("4.2.0")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "2.1.21"
}

android {
    namespace = "com.outfitgo.store"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.outfitgo.store"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val keystoreFile = project.rootProject.file("local.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        val apiKey = properties.getProperty("SHOPIFY_STORE_FRONT_ACCESS_TOKEN") ?: ""
        val adminApiKey = properties.getProperty("SHOPIFY_ADMIN_ACCESS_TOKEN") ?: ""

        buildConfigField(
            type = "String",
            name = "SHOPIFY_STORE_FRONT_ACCESS_TOKEN",
            value = apiKey
        )

        buildConfigField(
            type = "String",
            name = "SHOPIFY_ADMIN_ACCESS_TOKEN",
            value = adminApiKey
        )
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }

    apollo {

        val keystoreFile = project.rootProject.file("local.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        val apiKey = properties.getProperty("SHOPIFY_STORE_FRONT_ACCESS_TOKEN") ?: ""
        val adminApiKey = properties.getProperty("SHOPIFY_ADMIN_ACCESS_TOKEN") ?: ""


        service("storefront") {
            packageName.set("com.outfitgo.store.storefront")
            schemaFile.set(file("src/main/graphql/storefront/schema.graphqls"))
            introspection {
                endpointUrl.set("https://mad45-sv-and3.myshopify.com/api/2025-04/graphql.json")
                headers.set(
                    mapOf(
                        "X-Shopify-Storefront-Access-Token" to apiKey,
                        "Content-Type" to "application/json"
                    )
                )
            }
        }

        // Login feature makes a conflict when admin and storefront are together
        // to use Login without problems KEEP THE ADMIN COMMENTED
       /* service("admin") {
            packageName.set("com.outfitgo.store.admin")
            schemaFile.set(file("src/main/graphql/admin/schema.graphqls"))
            introspection {
                endpointUrl.set("https://mad45-sv-and3.myshopify.com/admin/api/2024-10/graphql.json")
                headers.set(
                    mapOf(
                        "X-Shopify-Access-Token" to adminApiKey,
                        "Content-Type" to "application/json"
                    )
                )
            }

        }*/
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
    implementation(libs.androidx.ui.text.google.fonts)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Splash
    implementation(libs.androidx.core.splashscreen)

    //apollo
    implementation(libs.apollo.runtime)

    //coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //compose navigation
    implementation(libs.androidx.navigation.compose)

    //viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //Lottie
    implementation(libs.lottie.compose)

    //Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

    // extended Icons
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // data store
    implementation("androidx.datastore:datastore-preferences:1.1.7")
}