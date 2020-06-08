package at.htl.motivapp.rest;

import at.htl.motivapp.model.Task;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GoApi {

    @GET("tasks")
    Call<Task[]> getTasks();

    @GET("tasks/{id}")
    Call<Task[]> getAllTasksFromIntention();

    
}
