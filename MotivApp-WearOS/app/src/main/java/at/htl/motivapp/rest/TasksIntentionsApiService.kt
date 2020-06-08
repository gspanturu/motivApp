package at.htl.motivapp.rest

import at.htl.motivapp.model.Intention
import at.htl.motivapp.model.Task
import retrofit2.Call
import retrofit2.http.*
import kotlinx.coroutines.Deferred

interface TasksIntentionsApiService {
    @GET("tasks")
    fun getTasks(): Deferred<List<Task>>

    @GET("tasks/{id}")
    fun getAllTasksFromIntention(@Path("id") intentionId: Int)

    @POST("tasks/add")
    fun postTask(@Body task: Task?)

    @POST("tasks/addMultiple")
    fun postMultipleTasks(@Body task: Deferred<List<Task>>)

    @PUT("tasks/update")
    fun putTask(@Body task: Task)

    @DELETE("tasks/delete")
    fun deleteIntention(@Body task: Task)

    @GET("intentions")
    fun getIntentions(): Deferred<List<Intention>>

    @GET("intentions/{id}")
    fun getIntentionById(@Path("id") intentionId: Int): Intention

    @POST("intentions/add")
    fun postIntention(@Body intention: Intention) : Intention

    @PUT("intentions/update")
    fun putIntention(@Body intention: Intention): Intention

    @DELETE("intentions/delete")
    fun deleteIntention(@Body intention: Intention): Intention
}