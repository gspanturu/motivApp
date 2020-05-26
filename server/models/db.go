package models

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
	db.Exec(schema)
	db.Close()

	fmt.Println("Successfully connected!")
}

func InsertIntention(i Intentions) {

	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		panic(err)
	}
	defer db.Close()

	db.Begin()

	sqlStatement := `INSERT INTO intentions (description, done)
		VALUES ($1, $2)
		RETURNING id`

	id := 0
	err = db.QueryRow(sqlStatement, i.Description, i.Done).Scan(&id)
	if err != nil {
		panic(err)
	}

	fmt.Println("New Intention with id: ", id)

	db.Close()
}

func UpdateIntention(i Intentions) {
	db, err := sql.Open("postgres", dataSourceName)
	if err != nil {
		panic(err)
	}
	defer db.Close()

	db.Begin()

	sqlStatement := `UPDATE intentions SET description = $2, done = $3 WHERE id = $1 RETURNING id`

	id := 0
	err = db.QueryRow(sqlStatement, i.Id, i.Description, i.Done).Scan(&id)
	if err != nil {
		panic(err)
	}

	fmt.Println("New Intention with id: ", id)

	db.Close()
}

func DeleteIntention(i Intentions) {
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
