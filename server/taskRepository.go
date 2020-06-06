package main

import (
	"database/sql"
	"fmt"
	"log"
	"motivapp/go/server/models"
)

var intentionId int
var date string
var done bool

func SelectTasks() []models.Task {

	var tasks []models.Task

	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		panic(err)
	}
	defer db.Close()

	db.Begin()

	sqlStatement := `Select * from tasks;`

	rows, err := db.Query(sqlStatement)
	if err != nil {
		log.Fatal(err)
	}
	defer rows.Close()

	var t models.Task

	for rows.Next() {
		err := rows.Scan(&id, &intentionId, &date, &done)
		if err != nil {
			log.Fatal(err)
		}

		t.Id = id
		t.IntentionId = intentionId
		t.Date = date
		t.Done = done

		tasks = append(tasks, t)
	}

	db.Close()

	return tasks
}

func SelectTasksByIntentionId(intentionÍd int) []models.Task {
	var tasks []models.Task

	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		panic(err)
	}
	defer db.Close()

	db.Begin()

	sqlStatement := `Select * from tasks where intentionId = $1;`

	rows, err := db.Query(sqlStatement, intentionÍd)
	if err != nil {
		log.Fatal(err)
	}
	defer rows.Close()

	var t models.Task

	for rows.Next() {
		err := rows.Scan(&id, &intentionId, &date, &done)
		if err != nil {
			log.Fatal(err)
		}

		t.Id = id
		t.IntentionId = intentionId
		t.Date = date
		t.Done = done

		tasks = append(tasks, t)
	}

	db.Close()

	return tasks
}

func InsertTask(t models.Task) {

	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		panic(err)
	}
	defer db.Close()

	db.Begin()

	sqlStatement := `INSERT INTO tasks (intentionId, date, done)
		VALUES ($1, $2, $3)
		RETURNING id`

	id := 0
	err = db.QueryRow(sqlStatement, t.IntentionId, t.Date, t.Done).Scan(&id)
	if err != nil {
		panic(err)
	}

	fmt.Println("New Task with id: ", id)

	db.Close()
}

func InsertTasks(t []models.Task) {

	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		panic(err)
	}
	defer db.Close()

	db.Begin()

	sqlStatement := `INSERT INTO tasks (intentionId, date, done)
		VALUES (Select id from intentions where id = $1), $2, $3)
		RETURNING id`

	i := 0
	id := 0
	err = db.QueryRow(sqlStatement, t[i].IntentionId, t[i].Date, t[i].Done).Scan(&id)
	if err != nil {
		panic(err)
	}

	fmt.Println("New Intention with id: ", id)

	db.Close()
}

func UpdateTask(t models.Task) {
	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		panic(err)
	}
	defer db.Close()

	db.Begin()

	sqlStatement := `UPDATE tasks SET done = $2 WHERE id = $1 RETURNING id`

	id := 0
	err = db.QueryRow(sqlStatement, t.Id, t.Done).Scan(&id)
	if err != nil {
		panic(err)
	}

	fmt.Println("Updated Task with id: ", id)

	db.Close()
}

func DeleteTask(t models.Task) {
	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		panic(err)
	}
	defer db.Close()

	db.Begin()

	sqlStatement := `Delete from tasks WHERE id = $1`

	_, err = db.Exec(sqlStatement, t.Id)
	if err != nil {
		panic(err)
	}

	fmt.Println("Deleted Task with id: ", t.Id)

	db.Close()
}
