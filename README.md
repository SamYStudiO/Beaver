# !!!WORK IN PROGRESS!!!

# beaver

Android Project template using [MVVM](https://developer.android.com/topic/libraries/architecture/index.html) with [Dagger 2](https://github.com/google/dagger).

Also includes a bunch of common use libraries (may be easily replaced or removed) :
- [Firebase app indexing](https://firebase.google.com/docs/app-indexing/)
- [Firebase crashlytics](https://firebase.google.com/docs/crashlytics/)
- [AccountManager](https://developer.android.com/reference/android/accounts/AccountManager.html)
- [Retrofit](https://github.com/square/retrofit)
- [Glide](https://github.com/bumptech/glide)
- [RxJava](https://github.com/ReactiveX/RxJava)
- [Android State](https://github.com/evernote/android-state)
- [Timber](https://github.com/JakeWharton/timber)

#### !!! Build Warning !!!

Due to kapt you may encountered a build error ([reference](https://github.com/gen0083/KotlinDaggerDataBinding/blob/master/README.md#what-causes)) :
   
    Error:(7, 13) error: package error does not exist
    Error:(11, 24) error: cannot find symbol class NonExistentClass
    Error:(23, 10) error: cannot find symbol class NonExistentClass
    Error:(34, 17) error: cannot find symbol class NonExistentClass

To solve this from your build.gradle file replace :

    kapt "com.google.dagger:dagger-compiler:2.14.1"
    kapt "com.google.dagger:dagger-android-processor:2.14.1"
    kapt "com.github.bumptech.glide:compiler:4.6.1"
    
with :

    annotationProcessor "com.google.dagger:dagger-compiler:2.14.1"
    annotationProcessor "com.google.dagger:dagger-android-processor:2.14.1"
    annotationProcessor "com.github.bumptech.glide:compiler:4.6.1"
    
Build project again then replace with :

    annotationProcessor "com.google.dagger:dagger-compiler:2.14.1"
    annotationProcessor "com.google.dagger:dagger-android-processor:2.14.1"
    kapt "com.github.bumptech.glide:compiler:4.6.1"
    
Build project again finally replace and build with :   

    kapt "com.google.dagger:dagger-compiler:2.14.1"
    kapt "com.google.dagger:dagger-android-processor:2.14.1"
    kapt "com.github.bumptech.glide:compiler:4.6.1"

This has to be done each time project is cleaned up, this can be solve as well by removing OkHttpAppGlideModule or completly remove Glide.
