package at.htl.motivapp.model;

public class Task {
    private int intentionId; //über IntentionID Title der Intention nehmen und benutzen
    private String title = "";
    private int id;
    private String date = "";
    private boolean done = false;

    public Task(int id, int intentionId, String date, boolean done){
        //this.title = title;
        this.id = id;
        this.intentionId = intentionId;
        this.date = date;
        this.done = done;
    }

    public int getIntentionId() {
        return intentionId;
    }

    public void setIntentionId(int intentionId) {
        this.intentionId = intentionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
