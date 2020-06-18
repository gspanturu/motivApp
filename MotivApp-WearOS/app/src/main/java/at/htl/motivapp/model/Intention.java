package at.htl.motivapp.model;

public class Intention {
    private int intentionId;
    private String description;
    private String googleId;

    public Intention(int intentionId, String description, String googleId) {
        this.intentionId = intentionId;
        this.description = description;
        this.googleId = googleId;
    }

    public int getIntentionId() {
        return intentionId;
    }

    public void setIntentionId(int intentionId) {
        this.intentionId = intentionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
}
