@file:Suppress("unused")

import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    // android
    const val core_ktx = "androidx.core:core-ktx:${Versions.core}"
    const val core_splashscreen = "androidx.core:core-splashscreen:${Versions.core_splashscreen}"
    const val startup = "androidx.startup:startup-runtime:${Versions.startup}"
    const val activity = "androidx.activity:activity-ktx:${Versions.activity}"
    const val fragment = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val preference_ktx = "androidx.preference:preference-ktx:${Versions.preference}"
    const val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    const val lifecycle_viewmodel_ktx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycle_runtime_ktx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycle_common = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val lifecycle_reactivestreams_ktx =
        "androidx.lifecycle:lifecycle-reactivestreams-ktx:${Versions.lifecycle}"
    const val lifecycle_service = "androidx.lifecycle:lifecycle-service:${Versions.lifecycle}"
    const val lifecycle_process = "androidx.lifecycle:lifecycle-process:${Versions.lifecycle}"
    const val arch_core_testing = "androidx.arch.core:core-testing:${Versions.arch_version}"
    const val navigation_fragment_ktx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val room = "androidx.room:room-ktx:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    const val room_testing = "androidx.room:room-testing:${Versions.room}"
    const val work = "androidx.work:work-runtime-ktx:${Versions.work}"
    const val datastore = "androidx.datastore:datastore:${Versions.datastore}"
    const val protobuf = "com.google.protobuf:protobuf-javalite:${Versions.protobuf}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val coroutines_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val coroutines_test =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"

    // firebase
    const val firebase_bom = "com.google.firebase:firebase-bom:${Versions.firebase_bom}"
    const val firebase_appindexing = "com.google.firebase:firebase-appindexing"
    const val firebase_crashlytics_ktx = "com.google.firebase:firebase-crashlytics-ktx"
    const val firebase_analytics_ktx = "com.google.firebase:firebase-analytics-ktx"
    const val firebase_perf_ktx = "com.google.firebase:firebase-perf-ktx"

    // dagger
    const val hilt_android = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hilt_compiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val hilt_android_testing = "com.google.dagger:hilt-android-testing:${Versions.hilt}"
    const val hilt_navigation_fragment =
        "androidx.hilt:hilt-navigation-fragment:${Versions.hilt_androidx}"
    const val hilt_work = "androidx.hilt:hilt-work:${Versions.hilt_androidx}"
    const val hilt_androidx_compiler = "androidx.hilt:hilt-compiler:${Versions.hilt_androidx}"

    // network
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp3}"
    const val okhttp_logging_interceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp3}"
    const val okhttp_mockwebserver = "com.squareup.okhttp3:mockwebserver:${Versions.okhttp3}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit2}"
    const val retrofit_converter_gson =
        "com.squareup.retrofit2:converter-gson:${Versions.retrofit2}"
    const val retrofit_adapter_rxjava3 =
        "com.squareup.retrofit2:adapter-rxjava3:${Versions.retrofit2}"
    const val coil = "io.coil-kt:coil:${Versions.coil}"

    // misc
    const val insetter = "dev.chrisbanes.insetter:insetter:${Versions.insetter}"
    const val permissionlauncher =
        "net.samystudio.permissionlauncher:permissionlauncher-ktx:${Versions.permissionlauncher}"
    const val flow_binding =
        "io.github.reactivecircus.flowbinding:flowbinding-android:${Versions.flow_binding}"

    // debug
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val leakcanary_android =
        "com.squareup.leakcanary:leakcanary-android:${Versions.leakcanary}"

    // test
    const val junit = "junit:junit:${Versions.junit}"
    const val test_core = "androidx.test:core:${Versions.test}"
    const val test_rules = "androidx.test:runner:${Versions.test}"
    const val test_runner = "androidx.test:runner:${Versions.test}"
    const val test_junit = "androidx.test.ext:junit:${Versions.test_junit}"
    const val test_truth = "androidx.test.ext:truth:${Versions.test}"
    const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val espresso_contrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    const val espresso_intents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
    const val espresso_accessibility =
        "androidx.test.espresso:espresso-accessibility:${Versions.espresso}"
    const val espresso_web = "androidx.test.espresso:espresso-web:${Versions.espresso}"
    const val espresso_idling_concurrent =
        "androidx.test.espresso:idling:idling-concurrent:${Versions.espresso}"
    const val espresso_idling_resource =
        "androidx.test.espresso:espresso-idling-resource:${Versions.espresso}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val mockito_core = "org.mockito:mockito-core:${Versions.mockito}"
    const val mockito_android = "org.mockito:mockito-android:${Versions.mockito}"
}

fun DependencyHandler.base() {
    implementation(Dependencies.core_ktx)
    implementation(Dependencies.core_splashscreen)
    implementation(Dependencies.startup)
    implementation(Dependencies.activity)
    implementation(Dependencies.fragment)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.preference_ktx)
    implementation(Dependencies.constraintlayout)
    implementation(Dependencies.datastore)
    implementation(Dependencies.protobuf)
    implementation(Dependencies.material)
    implementation(Dependencies.coroutines_android)
    testImplementation(Dependencies.coroutines_test)
}

fun DependencyHandler.lifecycle() {
    implementation(Dependencies.lifecycle_viewmodel_ktx)
    implementation(Dependencies.lifecycle_runtime_ktx)
    implementation(Dependencies.lifecycle_common)
    implementation(Dependencies.lifecycle_reactivestreams_ktx)
    implementation(Dependencies.lifecycle_process)
    //implementation(Dependencies.lifecycle_service)
    testImplementation(Dependencies.arch_core_testing)
}

fun DependencyHandler.navigation() {
    implementation(Dependencies.navigation_fragment_ktx)
    implementation(Dependencies.navigation_ui_ktx)
}

fun DependencyHandler.room() {
    implementation(Dependencies.room)
    kapt(Dependencies.room_compiler)
    testImplementation(Dependencies.room_testing)
}

fun DependencyHandler.work() {
    implementation(Dependencies.work)
}

fun DependencyHandler.firebase() {
    implementation(platform(Dependencies.firebase_bom))
    implementation(Dependencies.firebase_appindexing)
    implementation(Dependencies.firebase_crashlytics_ktx)
    implementation(Dependencies.firebase_analytics_ktx)
    implementation(Dependencies.firebase_perf_ktx)
}

fun DependencyHandler.dagger() {
    implementation(Dependencies.hilt_android)
    kapt(Dependencies.hilt_compiler)
    testImplementation(Dependencies.hilt_android_testing)
    androidTestImplementation(Dependencies.hilt_android_testing)
    implementation(Dependencies.hilt_navigation_fragment)
    implementation(Dependencies.hilt_work)
    kapt(Dependencies.hilt_androidx_compiler)
}

fun DependencyHandler.network() {
    implementation(Dependencies.okhttp)
    implementation(Dependencies.okhttp_logging_interceptor)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofit_converter_gson)
    implementation(Dependencies.retrofit_adapter_rxjava3)
    implementation(Dependencies.coil)
    testImplementation(Dependencies.okhttp_mockwebserver)
}

fun DependencyHandler.debug() {
    implementation(Dependencies.timber)
    debugImplementation(Dependencies.leakcanary_android)
}

fun DependencyHandler.test() {
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.robolectric)
    testImplementation(Dependencies.mockito_core)
}

fun DependencyHandler.androidTest() {
    androidTestImplementation(Dependencies.test_core)
    androidTestImplementation(Dependencies.test_runner)
    androidTestImplementation(Dependencies.test_rules)
    androidTestImplementation(Dependencies.test_junit)
    androidTestImplementation(Dependencies.test_truth)
    androidTestImplementation(Dependencies.espresso_core)
    //androidTestImplementation(Dependencies.espresso_contrib)
    //androidTestImplementation(Dependencies.espresso_intents)
    //androidTestImplementation(Dependencies.espresso_accessibility)
    //androidTestImplementation(Dependencies.espresso_web)
    //androidTestImplementation(Dependencies.espresso_idling_concurrent)
    //androidTestImplementation(Dependencies.espresso_idling_resource)
    androidTestImplementation(Dependencies.mockito_android)
}

private fun DependencyHandler.implementation(dep: Any) {
    add("implementation", dep)
}

private fun DependencyHandler.kapt(dep: Any) {
    add("kapt", dep)
}

private fun DependencyHandler.compileOnly(dep: Any) {
    add("compileOnly", dep)
}

private fun DependencyHandler.debugImplementation(dep: Any) {
    add("debugImplementation", dep)
}

private fun DependencyHandler.testImplementation(dep: Any) {
    add("testImplementation", dep)
}

private fun DependencyHandler.androidTestImplementation(dep: Any) {
    add("androidTestImplementation", dep)
}

private fun DependencyHandler.kaptAndroidTest(dep: Any) {
    add("kaptAndroidTest", dep)
}

private fun DependencyHandler.kaptTest(dep: Any) {
    add("kaptTest", dep)
}
