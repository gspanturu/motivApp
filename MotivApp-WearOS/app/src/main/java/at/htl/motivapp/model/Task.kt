package at.htl.motivapp.model

class Task(
    title: String,
    id: String,
    intentionId: Int,
    date: String,
    done: Boolean
) {
    lateinit var title: String
    var intentionId //über IntentionID Title der Intention nehmen und benutzen
            : Int

    var id: String
    var date = ""
    var isDone = false

    init {
        this.title = title
        this.id = id
        this.intentionId = intentionId
        this.date = date
        isDone = done
    }
}