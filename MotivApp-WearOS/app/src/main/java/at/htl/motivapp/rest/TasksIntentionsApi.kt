package at.htl.motivapp.rest

import at.htl.motivapp.rest.TasksIntentionsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TasksIntentionsApi {
    var retrofit = Retrofit.Builder()
        .baseUrl("http://10.10.203.194:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitService: TasksIntentionsApiService by lazy { retrofit.create(TasksIntentionsApiService::class.java) }
}