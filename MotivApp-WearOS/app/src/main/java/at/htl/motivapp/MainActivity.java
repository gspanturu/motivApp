package at.htl.motivapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import at.htl.motivapp.model.Task;
import at.htl.motivapp.rest.GoApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends WearableActivity {

    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:9080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GoApi goApi = retrofit.create(GoApi.class);

        Call<Task[]> call= goApi.getTasks();
        call.enqueue(new Callback<Task[]>() {
            @Override
            public void onResponse(Call<Task[]> call, Response<Task[]> response) {

            }

            @Override
            public void onFailure(Call<Task[]> call, Throwable t) {
                Log.d("failed get tasks", "failed get tasks");
            }
        });



        listView =  findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    displaySpeechScreen();
                } else {
                    Task task = (Task) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getApplicationContext(), MarkActivity.class);
                    intent.putExtra("id", task.getId());
                    startActivity(intent);
                }
            }
        });
        updateUI();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateUI();
    }

    private void updateUI() {
        ArrayList<Task> tasks = Helper.getAllGoals(this);

       /* goals.add(0, new Goal("0", "drink water"));
        goals.add(1, new Goal("1", "eat food"));
*/
       tasks.add(0, new Task(0,0, "08.06.2020", true));
        listView.setAdapter(new ListViewAdapter(this, 0, tasks));

    }

    public void displaySpeechScreen(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What is the title?");
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK){
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String message = results.get(0);
            Task task = new Task(0, 0, "07.06.2020", false);

            Helper.saveGoal(task, this);

            Helper.displayConfirmation("Goal saved!", this);
            updateUI();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}
