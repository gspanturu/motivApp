package at.htl.motivapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.wear.activity.ConfirmationActivity;

import java.util.ArrayList;
import java.util.Map;

public class Helper {
    public static String saveGoal(Goal goal, Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String id = String.valueOf(System.currentTimeMillis());
        editor.putString(id, goal.getTitle());

        editor.commit();

        return id;
    }

    public static ArrayList<Goal> getAllGoals(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        ArrayList<Goal> goals = new ArrayList<>();
        Map<String, ?> key = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : key.entrySet()){
            String savedData = (String) entry.getValue();

            if (savedData != null){
                Goal goal = new Goal(entry.getKey(), savedData);
                goals.add(goal);
            }
        }
        return goals;
    }

    public static void markGoalCompleted(String id, Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(id);
        editor.commit();
    }

    public static void displayConfirmation(String message, Context context){
        Intent intent = new Intent(context, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, message);
        context.startActivity(intent);
    }
}
