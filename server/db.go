package main

import (
	"database/sql"
	"fmt"

	_ "github.com/lib/pq"
)

var db *sql.DB

const (
	host     = "localhost"
	port     = 5432
	user     = "postgres"
	password = "magicalpassword"
	dbname   = "MotivApp_Database"
)

var schemaIntentions = `
CREATE TABLE intentions (
    id SERIAL PRIMARY KEY,
	description text,
	googleId text
)`

var schemaTasks = `
CREATE TABLE tasks (
	id SERIAL PRIMARY KEY,
	intentionId int references intentions(id),
	date text,
	done boolean
)`

var dataSourceName = fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=disable", host, port, user, password, dbname)

func InitDB() {
	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		panic(err)
	}

	defer db.Close()

	err = db.Ping()
	if err != nil {
		panic(err)
	}

	db.Begin()
	db.Exec(schemaIntentions)
	db.Exec(schemaTasks)
	db.Close()

	fmt.Println("Successfully connected!")
}
