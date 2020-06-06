package models

import "strconv"

type Intention struct {
	Id          int `db:"id"`
	Description string
	GoogleId    string
}

func IntentionsToJson(i Intention) string {
	return `{ "id": ` + strconv.Itoa(i.Id) + `, "description": "` + i.Description + `", "googleId": "` + i.GoogleId + `" }`
}
