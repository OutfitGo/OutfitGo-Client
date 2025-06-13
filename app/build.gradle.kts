import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.apollographql.apollo").version("4.2.0")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "2.1.21"
    id("com.google.gms.google-services")
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
        val currencyApiKey = properties.getProperty("CURRENCY_API_KEY") ?: ""
        val mapsApiKey = properties.getProperty("GOOGLE_MAPS_API_KEY") ?: ""
        manifestPlaceholders["MAPS_API_KEY"] = properties.getProperty("GOOGLE_MAPS_API_KEY") ?: ""

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

        buildConfigField(
            type = "String",
            name = "CURRENCY_API_KEY",
            value = currencyApiKey
        )

        buildConfigField(
            type = "String",
            name = "GOOGLE_MAPS_API_KEY",
            value = mapsApiKey
        )

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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

}

apollo {
    val keystoreFile = project.rootProject.file("local.properties")
    val properties = Properties()
    properties.load(keystoreFile.inputStream())

    val apiKey = properties.getProperty("SHOPIFY_STORE_FRONT_ACCESS_TOKEN") ?: ""

    service("storefront") {
        packageName.set("com.outfitgo.store.storefront")
        schemaFile.set(file("src/main/graphql/storefront/schema.graphqls"))
        sourceFolder.set("storefront")
        introspection {
            endpointUrl.set("https://mad-and2-sv.myshopify.com/api/2025-04/graphql.json")
            headers.set(
                mapOf(
                    "X-Shopify-Storefront-Access-Token" to apiKey,
                    "Content-Type" to "application/json"
                )
            )
        }
    }
}

apollo {

    val keystoreFile = project.rootProject.file("local.properties")
    val properties = Properties()
    properties.load(keystoreFile.inputStream())

    val adminApiKey = properties.getProperty("SHOPIFY_ADMIN_ACCESS_TOKEN") ?: ""

    service("admin") {
        packageName.set("com.outfitgo.store.admin")
        schemaFile.set(file("src/main/graphql/admin/schema.graphqls"))
        sourceFolder.set("admin")
        introspection {
            endpointUrl.set("https://mad-and2-sv.myshopify.com/admin/api/2024-10/graphql.json")
            headers.set(
                mapOf(
                    "X-Shopify-Access-Token" to adminApiKey,
                    "Content-Type" to "application/json"
                )
            )
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
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.places)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("com.google.android.material:material:1.2.0")
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

    //ktor client
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    //datastore
    implementation(libs.androidx.datastore.preferences)

    // extended Icons
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:33.15.0"))
    // firestore
    implementation("com.google.firebase:firebase-firestore")

    //Google Maps
    implementation (libs.maps.compose)

}