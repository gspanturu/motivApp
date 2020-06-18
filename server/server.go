package main

import (
	"log"
	"net/http"

	"github.com/gorilla/handlers"
	"github.com/gorilla/mux"
)

func startServer() {
	r := mux.NewRouter()
	api := r.PathPrefix("/api/v1").Subrouter()

	api.HandleFunc("/intentions", getIntentions).Methods(http.MethodGet)
	api.HandleFunc("/intentions/{id}", getIntentionById).Methods(http.MethodGet)
	api.HandleFunc("/intentions/add", postIntention).Methods(http.MethodPost)
	api.HandleFunc("/intentions/update", putIntention).Methods(http.MethodPut)
	api.HandleFunc("/intentions/delete", deleteIntention).Methods(http.MethodDelete)

	api.HandleFunc("/tasks", getTasks).Methods(http.MethodGet)
	api.HandleFunc("/tasks/{id}", getAllTasksFromIntention).Methods(http.MethodGet)
	api.HandleFunc("/tasks/add", postTask).Methods(http.MethodPost)
	api.HandleFunc("/tasks/addMultiple", postTasks).Methods(http.MethodPost)
	api.HandleFunc("/tasks/update", putTask).Methods(http.MethodPut)
	api.HandleFunc("/tasks/delete", deleteTask).Methods(http.MethodDelete)

	api.HandleFunc("", notFound)
	log.Fatal(http.ListenAndServe(":8080", handlers.CORS()(r)))
}
