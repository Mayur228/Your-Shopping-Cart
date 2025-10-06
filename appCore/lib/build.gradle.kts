plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization.android)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    //koin
    implementation(libs.koin)
    implementation(libs.kspAnnotation)
    ksp(libs.kspCompiler)

    //Ktor
    implementation(libs.ktor.serialization)

    /*//Firebase
    implementation(platform(libs.firebase.bom))
    //implementation(libs.firebase.firestore)
    implementation(libs.firebase.firestore.ktx)*/

    //Coroutine
    implementation(libs.kotlinx.coroutines.play.services)
}
