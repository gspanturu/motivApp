package at.htl.motivapp;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;
import android.widget.ProgressBar;

public class MarkActivity extends WearableActivity implements DelayedConfirmationView.DelayedConfirmationListener {

    DelayedConfirmationView delayedConfirmationView;
    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        delayedConfirmationView = findViewById(R.id.delayed_confirm);
        delayedConfirmationView.setListener(this);
        delayedConfirmationView.setTotalTimeMs(1000);
        delayedConfirmationView.start();
        // Enables Always-on
        setAmbientEnabled();
    }

    @Override
    public void onTimerFinished(View v) {
        spinner.setVisibility(View.GONE);
        Helper.displayConfirmation("You achieved your goal!", this);
        String id = getIntent().getStringExtra("id");
        Helper.markGoalCompleted(id, this);
        finish();
    }

    @Override
    public void onTimerSelected(View v) {
        spinner.setVisibility(View.GONE);
        Helper.displayConfirmation("Cancelled", this);
        delayedConfirmationView.reset();
        finish();
    }
}
