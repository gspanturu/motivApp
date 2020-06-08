package at.htl.motivapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import androidx.wear.activity.ConfirmationActivity;

import java.util.ArrayList;
import java.util.Map;

import at.htl.motivapp.model.Task;

public class Helper {
    public static String saveGoal(Task task, Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String id = String.valueOf(System.currentTimeMillis());
        editor.putString(id, task.getTitle());

        editor.apply();

        return id;
    }

    public static ArrayList<Task> getAllGoals(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        ArrayList<Task> tasks = new ArrayList<>();
        Map<String, ?> key = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : key.entrySet()){
            String savedData = (String) entry.getValue();

            if (savedData != null){
                Task task = new Task(entry.getKey(), savedData);
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static void markGoalCompleted(String id, Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(id);
        editor.apply();
    }

    public static void displayConfirmation(String message, Context context){
        Intent intent = new Intent(context, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, message);
        context.startActivity(intent);
    }
}
