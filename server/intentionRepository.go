package main

import (
	"database/sql"
	"fmt"
	"log"
	"motivapp/go/server/models"
)

var id int
var description string
var googleId string

func SelectIntentions() []models.Intention {

	var intentions []models.Intention

	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		panic(err)
	}
	defer db.Close()

	db.Begin()

	sqlStatement := `Select * from intentions;`

	rows, err := db.Query(sqlStatement)
	if err != nil {
		log.Fatal(err)
	}
	defer rows.Close()

	var i models.Intention

	for rows.Next() {
		err := rows.Scan(&id, &description, &googleId)
		if err != nil {
			log.Fatal(err)
		}

		i.Id = id
		i.Description = description
		i.GoogleId = googleId

		intentions = append(intentions, i)
	}

	db.Close()

	return intentions
}

func SelectIntentionById(id int) models.Intention {

	var intention models.Intention

	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		panic(err)
	}
	defer db.Close()

	db.Begin()

	sqlStatement := `Select * from intentions where id = $1;`

	rows, err := db.Query(sqlStatement, id)
	if err != nil {
		log.Fatal(err)
	}
	defer rows.Close()

	rows.Next()
	err = rows.Scan(&id, &description, &googleId)
	if err != nil {
		log.Fatal(err)
	}

	intention.Id = id
	intention.Description = description
	intention.GoogleId = googleId

	db.Close()

	return intention
}

func InsertIntention(i models.Intention) {

	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		panic(err)
	}
	defer db.Close()

	db.Begin()

	sqlStatement := `INSERT INTO intentions (description, googleId)
		VALUES ($1, $2)
		RETURNING id`

	id := 0
	err = db.QueryRow(sqlStatement, i.Description, i.GoogleId).Scan(&id)
	if err != nil {
		panic(err)
	}

	fmt.Println("New Intention with id: ", id)

	db.Close()
}

func UpdateIntention(i models.Intention) {
	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		panic(err)
	}
	defer db.Close()

	db.Begin()

	sqlStatement := `UPDATE intentions SET description = $2, googleId = $3 WHERE id = $1 RETURNING id`

	id := 0
	err = db.QueryRow(sqlStatement, i.Id, i.Description, i.GoogleId).Scan(&id)
	if err != nil {
		panic(err)
	}

	fmt.Println("Updated Intention with id: ", id)

	db.Close()
}

func DeleteIntention(i models.Intention) {
	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		panic(err)
	}
	defer db.Close()

	db.Begin()

	sqlStatement := `Delete from intentions WHERE id = $1`

	_, err = db.Exec(sqlStatement, i.Id)
	if err != nil {
		panic(err)
	}

	fmt.Println("Deleted intention with id: ", i.Id)

	db.Close()
}
