package at.htl.motivapp;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;

public class MarkActivity extends WearableActivity implements DelayedConfirmationView.DelayedConfirmationListener {

    DelayedConfirmationView delayedConfirmationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);

        delayedConfirmationView = findViewById(R.id.delayed_confirm);
        delayedConfirmationView.setListener(this);
        delayedConfirmationView.setTotalTimeMs(3000);
        delayedConfirmationView.start();
        // Enables Always-on
        setAmbientEnabled();
    }

    @Override
    public void onTimerFinished(View v) {
        Helper.displayConfirmation("You achieved your goal!", this);
        String id = getIntent().getStringExtra("id");
        Helper.markGoalCompleted(id, this);
        finish();
    }

    @Override
    public void onTimerSelected(View v) {
        Helper.displayConfirmation("Cancelled", this);
        delayedConfirmationView.reset();
        finish();
    }
}
