package net.samystudio.beaver.data.remote

import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import net.samystudio.beaver.data.ResultAsyncState
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.data.toResultAsyncState
import retrofit2.http.GET
import javax.inject.Inject

interface HomeApiInterface {
    @GET("home.json")
    fun home(): Single<Home>
}

@ActivityScoped
class HomeApiInterfaceImpl @Inject constructor(private val homeApiInterface: HomeApiInterface) {
    fun home(): Observable<ResultAsyncState<Home>> =
        homeApiInterface.home().toResultAsyncState()
}