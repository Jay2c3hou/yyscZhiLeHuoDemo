plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.yyscdemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.yyscdemo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // 圆角
    implementation("io.github.csdn-mobile:RoundView:1.8.0")

    // Scalable Size Unit
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("com.intuit.ssp:ssp-android:1.0.6")

    // 日期选择器
    implementation("com.github.limxing:DatePickerView:1.1.1")

    //权限请求框架
    implementation("com.tbruyelle.rxpermissions2:rxpermissions:0.9.4@aar")
    implementation("io.reactivex.rxjava2:rxandroid:2.0.2")
    implementation("io.reactivex.rxjava2:rxjava:2.0.0")

    //热门强大的图片加载器
    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    implementation("com.github.li-xiaojun:XPopup:2.10.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Gson Converter for Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp (Retrofit 的默认网络库)
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // OkHttp Logging Interceptor (可选，便于调试网络请求)
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
}