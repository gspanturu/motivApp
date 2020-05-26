package at.htl.motivapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends WearableActivity {

    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        listView =  findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*if (position == 0){
                    displaySpeechScreen();
                }
                else{*/
                    Goal goal = (Goal) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getApplicationContext(), MarkActivity.class);
                    intent.putExtra("id", goal.getId());
                    startActivity(intent);
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
        ArrayList<Goal> goals = Helper.getAllGoals(this);

       /* goals.add(0, new Goal("0", "drink water"));
        goals.add(1, new Goal("1", "eat food"));
*/
       goals.add(0, new Goal("", ""));
        listView.setAdapter(new ListViewAdapter(this, 0, goals));

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
            Goal goal = new Goal(null, message);

            Helper.saveGoal(goal, this);

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
