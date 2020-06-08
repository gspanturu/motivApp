package at.htl.motivapp.rest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TasksIntentionsRepository {

    suspend fun getTasksFromApi(){
        withContext(Dispatchers.IO){
            val deferredObjects = TasksIntentionsApi.retrofitService.getTasks()
        }
    }
}