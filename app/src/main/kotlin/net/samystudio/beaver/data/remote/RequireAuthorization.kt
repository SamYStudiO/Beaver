package net.samystudio.beaver.data.remote

import okhttp3.Request
import retrofit2.Invocation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequireAuthorization

fun <T : Annotation> Request.getAnnotation(annotationClass: Class<T>): T? =
    this.tag(Invocation::class.java)?.method()?.getAnnotation(annotationClass)

fun Request.isAuthorizationRequired(): Boolean =
    this.getAnnotation(RequireAuthorization::class.java) != null
