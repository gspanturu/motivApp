package models

import "strconv"

type Task struct {
	Id          int `db:"id"`
	IntentionId int
	Date        string
	Done        bool
}

func TaskToJson(t Task) string {
	return `{ "id": ` + strconv.Itoa(t.Id) + `, "intentionId": ` + strconv.Itoa(t.IntentionId) + `, "date": "` + t.Date + `", "done": ` + strconv.FormatBool(t.Done) + ` }`
}
