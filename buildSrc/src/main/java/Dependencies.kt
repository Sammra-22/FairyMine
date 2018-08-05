/**
 * Created by Sam22 on 2/18/18.
 */
object Versions {
    val app_version_code = 1
    val app_version_name = "1.0"
    val compile_sdk = 27
    val min_sdk = 14
    val target_sdk = 27
    val gradle = "3.0.1"
    val kotlin = "1.2.41"
    val supportLibrary = "27.1.1"
    val mockitoVersion = "2.12.0"
    val gradlePlugin = "3.1.2"
    val googleServices = "3.1.2"
    val playServices = "11.8.0"
    val retrofit = "2.3.0"
    val jodaTime = "2.9.9"
    val gsonFire = "1.8.0"
    val gson = "2.8.0"
    val okhttp = "3.9.0"
    val rxJava = "2.1.14"
    val rxAndroid = "2.0.2"
    val paperParcel = "2.0.1"
    val rxPreferences = "2.0.0-RC3"
    val dagger = "2.12"
    val constrainLayout = "1.0.2"
    val junit = "4.12"
    val testRunner = "1.0.1"
    val espresso = "3.0.1"
    val guava = "21.0"
    val timber = "4.1.0"
}

object Libs {
    val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val constrain_layout = "com.android.support.constraint:constraint-layout:${Versions.constrainLayout}"
    val test_junit = "junit:junit:${Versions.junit}"
    val mockito_inline = "org.mockito:mockito-inline:${Versions.mockitoVersion}"
    val mockito_core = "org.mockito:mockito-core:${Versions.mockitoVersion}"
    val test_runner = "com.android.support.test:runner:${Versions.testRunner}"
    val test_espresso_core = "com.android.support.test.espresso:espresso-core:${Versions.espresso}"
    val test_espresso_intents = "com.android.support.test.espresso:espresso-intents:${Versions.espresso}"
    val test_espresso_contrib = "com.android.support.test.espresso:espresso-contrib:${Versions.espresso}"
    val test_rules = "com.android.support.test:rules:${Versions.testRunner}"
    val test_json = "org.json:json:20180130"
    val test_mock_server = "com.squareup.okhttp3:mockwebserver:${Versions.okhttp}"
    val play_services = "com.google.android.gms:play-services-auth:${Versions.playServices}"

    // Android support libs
    val databinding_compiler = "com.android.databinding:compiler:${Versions.gradlePlugin}"
    val support_v13 = "com.android.support:support-v13:${Versions.supportLibrary}"
    val appcompat_v7 = "com.android.support:appcompat-v7:${Versions.supportLibrary}"
    val design = "com.android.support:design:${Versions.supportLibrary}"
    val cardview_v7 = "com.android.support:cardview-v7:${Versions.supportLibrary}"
    val recyclerview_v7 = "com.android.support:recyclerview-v7:${Versions.supportLibrary}"

    // Parcelable
    val paperparcel = "nz.bradcampbell:paperparcel:${Versions.paperParcel}"
    val paperparcel_kotlin = "nz.bradcampbell:paperparcel-kotlin:${Versions.paperParcel}"
    val paperparcel_compiler = "nz.bradcampbell:paperparcel-compiler:${Versions.paperParcel}"

    // Dagger (DI)
    val guava = "com.google.guava:guava:${Versions.guava}"
    val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    val dagger_support = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    val dagger_processor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
    val dagger_compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"

    // API libraries
    val joda_time = "net.danlew:android.joda:${Versions.jodaTime}"
    val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    val okhttp_log = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    val gson = "com.google.code.gson:gson:${Versions.gson}"
    val gson_fire = "io.gsonfire:gson-fire:${Versions.gsonFire}"
    val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofit2_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    val retrofit2_rxjava = "com.squareup.retrofit2:adapter-rxjava:${Versions.retrofit}"
    val retrofit2_scalars = "com.squareup.retrofit2:converter-scalars:${Versions.retrofit}"
    val retrofit2_rxAdapter = "com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0"

    // RxJava - RxAndroid
    val rxandroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    val rxjava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    val rxpreferences = "com.f2prateek.rx.preferences2:rx-preferences:${Versions.rxPreferences}"

    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
}