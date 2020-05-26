package models

var schema = `
CREATE TABLE intentions (
    id SERIAL PRIMARY KEY,
	description text,
	done boolean
)`

type Intentions struct {
	Id          int `db:"id"`
	Description string
	Done        bool
}
