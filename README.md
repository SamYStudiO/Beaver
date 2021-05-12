# Beaver

Android Project template using View Model [MVVM](https://developer.android.com/topic/libraries/architecture/index.html) and Repository Data pattern, with [Dagger 2 / Hilt](https://github.com/google/dagger) and [Jetpack (Androidx)](https://developer.android.com/jetpack/) libraries (navigation component, room, worker, etc...).

Template includes basic implementation for a launch screen, a home screen, an authentication screen and a profile screen.
Authentication is implemented using OAuth2 and automatically refresh token when a 401 HTTP exception is thrown.

Also features :
- An [AlertDialog](https://github.com/SamYStudiO/Beaver/blob/master/app/src/main/kotlin/net/samystudio/beaver/ui/common/dialog/AlertDialog.kt) implementation using DialogFragment that survive state lost and returns actions/results using [setFragmentResultListener API](https://developer.android.com/guide/fragments/communicate#fragment-result). Some [kotlin extensions](https://github.com/SamYStudiO/Beaver/blob/master/app/src/main/kotlin/net/samystudio/beaver/ui/common/dialog/AlertDialog.kt#L568) are available to make results even straightforward.
- Some [utilities](https://github.com/SamYStudiO/Beaver/tree/master/app/src/main/kotlin/net/samystudio/beaver/util).
- [Base classes](https://github.com/SamYStudiO/Beaver/tree/master/app/src/main/kotlin/net/samystudio/beaver/ui/base/adapter) for Adapter/ListAdapter and ViewHolder.
- Dynamic retrofit url to handle multiple server url (production, pre production, etc...), selector screen/dialog for server not implemented here though.
- [Structured values resources files](https://github.com/SamYStudiO/Beaver/tree/master/app/src/main/res/values) as recommended(themes, styles, shapes, types).
- Gradle kotlin DSL.

Common used libraries :
- [Firebase app indexing](https://firebase.google.com/docs/app-indexing/)
- [Firebase crashlytics](https://firebase.google.com/docs/crashlytics/)
- [Retrofit](https://github.com/square/retrofit)
- [Coil](https://github.com/coil-kt/coil)
- [RxJava3](https://github.com/ReactiveX/RxJava)
- [RxPreferences](https://github.com/f2prateek/rx-preferences)
- [Timber](https://github.com/JakeWharton/timber)

Package structure is inspired from [this](https://overflow.buffer.com/2016/09/26/android-rethinking-package-structure/) reading.