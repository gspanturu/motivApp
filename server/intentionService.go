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

var intentions models.Intention

func getIntentions(w http.ResponseWriter, r *http.Request) {
	var intentions = SelectIntentions()
	var intentionsString = "["

	for _, i := range intentions {
		intentionsString += models.IntentionsToJson(i)
		intentionsString += ", \n"
	}

	intentionsString += "]"

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(intentionsString))
}

func getIntentionById(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	id, err := strconv.Atoi(vars["id"])
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		return
	}

	var intention = SelectIntentionById(id)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(models.IntentionsToJson(intention)))
}

func postIntention(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()

	bodyBytes, err := ioutil.ReadAll(r.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	json.Unmarshal(bodyBytes, &intentions)

	fmt.Println(intentions)
	InsertIntention(intentions)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	w.Write([]byte(`Succesfull Insert`))
}

func putIntention(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()

	bodyBytes, err := ioutil.ReadAll(r.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	json.Unmarshal(bodyBytes, &intentions)

	fmt.Println(intentions)
	UpdateIntention(intentions)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusAccepted)
	w.Write([]byte(`Succesfull Update`))
}

func deleteIntention(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()

	bodyBytes, err := ioutil.ReadAll(r.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	json.Unmarshal(bodyBytes, &intentions)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`Succesfull Delete`))

	fmt.Println(intentions)
	DeleteIntention(intentions)
}

func notFound(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusNotFound)
	w.Write([]byte(`{"message": "not found"}`))
}
