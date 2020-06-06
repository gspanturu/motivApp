package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"motivapp/go/server/models"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
)

var task models.Task

func getTasks(w http.ResponseWriter, r *http.Request) {

	var tasks = SelectTasks()
	var tasksString = "["

	for _, t := range tasks {
		tasksString += models.TaskToJson(t)
		tasksString += ", \n"
	}

	tasksString += "]"

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(tasksString))
}

func getAllTasksFromIntention(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	intentionId, err := strconv.Atoi(vars["id"])
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		return
	}

	var tasks = SelectTasksByIntentionId(intentionId)
	var tasksString = "["

	for _, t := range tasks {
		tasksString += models.TaskToJson(t)
		tasksString += ", \n"
	}

	tasksString += "]"


	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(tasksString))
}

func postTask(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()

	bodyBytes, err := ioutil.ReadAll(r.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	json.Unmarshal(bodyBytes, &task)

	fmt.Println(task)
	InsertTask(task)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	w.Write([]byte(`Succesfull Insert`))
}

func postTasks(w http.ResponseWriter, r *http.Request) {
	var tasks []models.Task
	defer r.Body.Close()

	bodyBytes, err := ioutil.ReadAll(r.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	json.Unmarshal(bodyBytes, &tasks)

	fmt.Println(tasks)
	for _,t := range tasks {
		InsertTask(t)	
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	w.Write([]byte(`Succesfull Insert`))
}

func putTask(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()

	bodyBytes, err := ioutil.ReadAll(r.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	json.Unmarshal(bodyBytes, &task)

	fmt.Println(task)
	UpdateTask(task)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusAccepted)
	w.Write([]byte(`Succesfull Update`))
}

func deleteTask(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()

	bodyBytes, err := ioutil.ReadAll(r.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	json.Unmarshal(bodyBytes, &task)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`Succesfull Delete`))

	fmt.Println(task)
	DeleteTask(task)
}