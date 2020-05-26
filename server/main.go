package main

import (
	"motivapp/go/server/models"

	_ "github.com/lib/pq"
)

func main() {

	models.InitDB()

	startServer()
}
