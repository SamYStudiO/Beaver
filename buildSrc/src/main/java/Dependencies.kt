import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    // kotlin
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    // android
    const val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val preference_ktx = "androidx.preference:preference-ktx:${Versions.preference_ktx}"
    const val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    const val lifecycle_viewmodel_ktx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycle_livedata_ktx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycle_common = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val lifecycle_reactivestreams_ktx =
        "androidx.lifecycle:lifecycle-reactivestreams-ktx:${Versions.lifecycle}"
    const val lifecycle_service = "androidx.lifecycle:lifecycle-service:${Versions.lifecycle}"
    const val lifecycle_process = "androidx.lifecycle:lifecycle-process:${Versions.lifecycle}"
    const val navigation_fragment_ktx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    const val room_rxjava2 = "androidx.room:room-rxjava2:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    const val material = "com.google.android.material:material:${Versions.material}"

    // firebase
    const val firebase_appindexing =
        "com.google.firebase:firebase-appindexing:${Versions.firebase_appindexing}"
    const val firebase_crashlytics =
        "com.google.firebase:firebase-crashlytics-ktx:${Versions.firebase_crashlytics}"
    const val firebase_analytics =
        "com.google.firebase:firebase-analytics-ktx:${Versions.firebase_analytics}"
    const val firebase_perf = "com.google.firebase:firebase-perf-ktx:${Versions.firebase_perf}"

    // dagger
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val dagger_android = "com.google.dagger:dagger-android:${Versions.dagger}"
    const val dagger_android_support = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    const val dagger_compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val dagger_android_processor =
        "com.google.dagger:dagger-android-processor:${Versions.dagger}"
    const val assisted_inject_annotations =
        "com.squareup.inject:assisted-inject-annotations-dagger2:${Versions.assisted_inject}"
    const val assisted_inject_processor =
        "com.squareup.inject:assisted-inject-processor-dagger2:${Versions.assisted_inject}"
    const val hilt_android =
        "com.google.dagger:hilt-android:${Versions.hilt_android}"
    const val hilt_android_compiler =
        "com.google.dagger:hilt-android-compiler:${Versions.hilt_android}"
    const val hilt_lifecycle_viewmodel =
        "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hilt_androidx}"
    const val hilt_work =
        "androidx.hilt:hilt-work:${Versions.hilt_androidx}"
    const val hilt_androidx_compiler =
        "androidx.hilt:hilt-compiler:${Versions.hilt_androidx}"

    // reactive
    const val rxjava = "io.reactivex.rxjava3:rxjava:${Versions.rxjava}"
    const val rxandroid = "io.reactivex.rxjava3:rxandroid:${Versions.rxandroid}"
    const val rxkotlin = "io.reactivex.rxjava3:rxkotlin:${Versions.rxkotlin}"
    const val rxbinding = "com.jakewharton.rxbinding4:rxbinding:${Versions.rxbinding}"
    const val rxbinding_core =
        "com.jakewharton.rxbinding4:rxbinding-core:${Versions.rxbinding}"
    const val rxbinding_appcompat =
        "com.jakewharton.rxbinding4:rxbinding-appcompat:${Versions.rxbinding}"
    const val rxbinding_drawerlayout =
        "com.jakewharton.rxbinding4:rxbinding-drawerlayout:${Versions.rxbinding}"
    const val rxbinding_leanback =
        "com.jakewharton.rxbinding4:rxbinding-leanback:${Versions.rxbinding}"
    const val rxbinding_recyclerview =
        "com.jakewharton.rxbinding4:rxbinding-recyclerview:${Versions.rxbinding}"
    const val rxbinding_slidingpanelayout =
        "com.jakewharton.rxbinding4:rxbinding-slidingpanelayout:${Versions.rxbinding}"
    const val rxbinding_swiperefreshlayout =
        "com.jakewharton.rxbinding4:rxbinding-swiperefreshlayout:${Versions.rxbinding}"
    const val rxbinding_viewpager2 =
        "com.jakewharton.rxbinding4:rxbinding-viewpager2:${Versions.rxbinding}"
    const val rx_preferences =
        "com.f2prateek.rx.preferences2:rx-preferences:${Versions.rx_preferences}"
    const val rxpicasso = "net.samystudio.rxpicasso:rxpicasso-kotlin:${Versions.rxpicasso}"

    // network
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val okhttp_logging_interceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofit_converter_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofit_adapter_rxjava3 =
        "com.squareup.retrofit2:adapter-rxjava3:${Versions.retrofit}"
    const val picasso = "com.squareup.picasso:picasso:${Versions.picasso}"

    // misc
    const val permissionsdispatcher =
        "org.permissionsdispatcher:permissionsdispatcher:${Versions.permissionsdispatcher}"
    const val permissionsdispatcher_processor =
        "org.permissionsdispatcher:permissionsdispatcher-processor:${Versions.permissionsdispatcher}"

    // debug
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val leakcanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakcanary}"

    // test
    const val junit = "junit:junit:${Versions.junit}"
    const val test_core = "androidx.test:core:${Versions.test_core}"
    const val test_runner = "androidx.test:runner:${Versions.test_runner}"
    const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso_core}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val mockito_core = "org.mockito:mockito-core:${Versions.mockito}"
    const val mockito_android = "org.mockito:mockito-android:${Versions.mockito}"
    const val okhttp_mockwebserver = "com.squareup.okhttp3:mockwebserver:${Versions.okhttp}"
    const val arch_core_testing = "androidx.arch.core:core-testing:${Versions.arch_version}"
    const val room_testing = "androidx.room:room-testing:${Versions.room}"
    const val hilt_testing = "com.google.dagger:hilt-android-testing:${Versions.hilt_android}"
}

fun DependencyHandler.base() {
    implementation(Dependencies.kotlin)
    implementation(Dependencies.core_ktx)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.preference_ktx)
    implementation(Dependencies.constraintlayout)
    implementation(Dependencies.material)
}

fun DependencyHandler.lifecycle() {
    implementation(Dependencies.lifecycle_viewmodel_ktx)
    implementation(Dependencies.lifecycle_livedata_ktx)
    implementation(Dependencies.lifecycle_common)
    implementation(Dependencies.lifecycle_reactivestreams_ktx)
    testImplementation(Dependencies.arch_core_testing)
}

fun DependencyHandler.navigation() {
    implementation(Dependencies.navigation_fragment_ktx)
    implementation(Dependencies.navigation_ui_ktx)
}

fun DependencyHandler.room() {
    implementation(Dependencies.room_runtime)
    implementation(Dependencies.room_rxjava2)
    kapt(Dependencies.room_compiler)
    testImplementation(Dependencies.room_testing)
}

fun DependencyHandler.firebase() {
    implementation(Dependencies.firebase_appindexing)
    implementation(Dependencies.firebase_crashlytics)
    implementation(Dependencies.firebase_analytics)
    implementation(Dependencies.firebase_perf)
}

fun DependencyHandler.dagger() {
    implementation(Dependencies.dagger)
    implementation(Dependencies.dagger_android)
    implementation(Dependencies.dagger_android_support)
    kapt(Dependencies.dagger_compiler)
    kapt(Dependencies.dagger_android_processor)
    compileOnly(Dependencies.assisted_inject_annotations)
    kapt(Dependencies.assisted_inject_processor)
    implementation(Dependencies.hilt_android)
    kapt(Dependencies.hilt_android_compiler)
    androidTestImplementation(Dependencies.hilt_testing)
    implementation(Dependencies.hilt_lifecycle_viewmodel)
    implementation(Dependencies.hilt_work)
    kapt(Dependencies.hilt_androidx_compiler)
}

fun DependencyHandler.reactive() {
    implementation(Dependencies.rxjava)
    implementation(Dependencies.rxandroid)
    implementation(Dependencies.rxkotlin)
    implementation(Dependencies.rxbinding)
    implementation(Dependencies.rx_preferences)
    implementation(Dependencies.rxpicasso)
}

fun DependencyHandler.network() {
    implementation(Dependencies.okhttp)
    implementation(Dependencies.okhttp_logging_interceptor)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofit_converter_gson)
    implementation(Dependencies.retrofit_adapter_rxjava3)
    implementation(Dependencies.picasso)
    testImplementation(Dependencies.okhttp_mockwebserver)
}

fun DependencyHandler.debug() {
    implementation(Dependencies.timber)
    debugImplementation(Dependencies.leakcanary)
}

fun DependencyHandler.test() {
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.test_core)
    testImplementation(Dependencies.robolectric)
    testImplementation(Dependencies.mockito_core)
}

fun DependencyHandler.androidTest() {
    androidTestImplementation(Dependencies.test_runner)
    androidTestImplementation(Dependencies.espresso_core)
    androidTestImplementation(Dependencies.mockito_android)
}

private fun DependencyHandler.implementation(depName: String) {
    add("implementation", depName)
}

private fun DependencyHandler.kapt(depName: String) {
    add("kapt", depName)
}

private fun DependencyHandler.compileOnly(depName: String) {
    add("compileOnly", depName)
}

private fun DependencyHandler.debugImplementation(depName: String) {
    add("debugImplementation", depName)
}

private fun DependencyHandler.testImplementation(depName: String) {
    add("testImplementation", depName)
}

private fun DependencyHandler.androidTestImplementation(depName: String) {
    add("androidTestImplementation", depName)
}

private fun DependencyHandler.kaptAndroidTest(depName: String) {
    add("kaptAndroidTest", depName)
}

private fun DependencyHandler.kaptTest(depName: String) {
    add("kaptTest", depName)
}