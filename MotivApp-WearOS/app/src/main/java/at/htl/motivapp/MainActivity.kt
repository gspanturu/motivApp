package at.htl.motivapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.wearable.activity.WearableActivity
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
import at.htl.motivapp.Helper.displayConfirmation
import at.htl.motivapp.Helper.getAllGoals
import at.htl.motivapp.Helper.saveGoal
import at.htl.motivapp.model.Task

class MainActivity : WearableActivity() {
    lateinit var listView: ListView
    lateinit var textView: TextView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.list_view)
        textView = findViewById(R.id.text_view)
        listView.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                displaySpeechScreen()
            } else {
                val task =
                    parent.getItemAtPosition(position) as Task
                val intent = Intent(applicationContext, MarkActivity::class.java)
                intent.putExtra("id", task.id)
                startActivity(intent)
            }
        })
        updateUI()
    }

    override fun onPostResume() {
        super.onPostResume()
        updateUI()
    }

    private fun updateUI() {
        val tasks =
            getAllGoals(this)

        /* goals.add(0, new Goal("0", "drink water"));
        goals.add(1, new Goal("1", "eat food"));*/tasks.add(
            0,
            Task("", "0", 0, "08.06.2020", true)
        )
        listView!!.adapter = ListViewAdapter(this, 0, tasks)
    }

    fun displaySpeechScreen() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What is the title?")
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent
    ) {
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            val results: List<String> =
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val message = results[0]
            val task =
                Task(message, "0", 0, "07.06.2020", false)
            saveGoal(task, this)
            displayConfirmation("Goal saved!", this)
            updateUI()
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }
}