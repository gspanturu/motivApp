package at.htl.motivapp

import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import androidx.wear.activity.ConfirmationActivity
import at.htl.motivapp.model.Intention
import at.htl.motivapp.model.Task
import java.util.*

object Helper {

    fun saveGoal(
        task: Task,
        context: Context?
    ): String {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val id = System.currentTimeMillis().toString()
        editor.putString(id, task.title)
        editor.apply()
        return id
    }


    fun getAllGoals(context: Context?): ArrayList<Task> {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val tasks =
            ArrayList<Task>()
        val key = sharedPreferences.all
        for ((key1, value) in key) {
            val savedData = value as String?
            println("saved data: $savedData")
            if (savedData != null) {
                val intention = Intention(0, "irgendwos schoffn", "abcd")
                val task =
                    Task(savedData, key1!!, 0, "08.06.2020", true)
                tasks.add(task)
            }
        }
        return tasks
    }

     fun markGoalCompleted(id: String?, context: Context?) {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.remove(id)
        editor.apply()
    }


     fun displayConfirmation(
        message: String?,
        context: Context
    ) {
        val intent =
            Intent(context, ConfirmationActivity::class.java)
        intent.putExtra(
            ConfirmationActivity.EXTRA_ANIMATION_TYPE,
            ConfirmationActivity.SUCCESS_ANIMATION
        )
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, message)
        context.startActivity(intent)
    }
}