package at.htl.motivapp

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.support.wearable.view.DelayedConfirmationView
import android.support.wearable.view.DelayedConfirmationView.DelayedConfirmationListener
import android.view.View
import android.widget.ProgressBar
import at.htl.motivapp.Helper.displayConfirmation
import at.htl.motivapp.Helper.markGoalCompleted

class MarkActivity : WearableActivity(), DelayedConfirmationListener {
    lateinit var delayedConfirmationView: DelayedConfirmationView
    private var spinner: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark)
        spinner = findViewById<View>(R.id.progressBar) as ProgressBar
        spinner!!.visibility = View.VISIBLE
        delayedConfirmationView = findViewById(R.id.delayed_confirm)
        delayedConfirmationView.setListener(this)
        delayedConfirmationView.setTotalTimeMs(1000)
        delayedConfirmationView.start()
        // Enables Always-on
        setAmbientEnabled()
    }

    override fun onTimerFinished(v: View) {
        spinner!!.visibility = View.GONE
        displayConfirmation("You achieved your goal!", this)
        val id = intent.getStringExtra("id")
        markGoalCompleted(id, this)
        finish()
    }

    override fun onTimerSelected(v: View) {
        spinner!!.visibility = View.GONE
        displayConfirmation("Cancelled", this)
        delayedConfirmationView!!.reset()
        finish()
    }
}