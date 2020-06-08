<template>
  <div id="app">
    <add-motivation-modal @saveMotivation="saveMotivation"/>


    <div class="header">
      <h1>Motivapp</h1>
      <button class="button-add" @click="addMotivation"></button>
      <div class="google">
        <div class="g-signin2" data-onsuccess="onSignIn" data-onfailure="onFailure"></div>
        <a href="#" onclick="signOut;">Sign out</a>
      </div>
    </div>
    <MotivationList ref="motivationToAdd"/>

  </div>
</template>

<script>
import MotivationList from "./components/MotivationList"
import AddMotivationModal from "./components/AddMotivationModal"
export default {
  name: 'App',
  components: {
    MotivationList,
    AddMotivationModal
  },
  data:() =>({
    
  }),
  methods: {
    addMotivation(){
      this.$modal.show('add-motivation-modal')
      //this.$refs.motivationList.addMotivation()
    },
    onSignIn(googleUser) {
      alert("test")
      var profile = googleUser.getBasicProfile();
      console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
      console.log('Name: ' + profile.getName());
      console.log('Image URL: ' + profile.getImageUrl());
      console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
    },
    
    /*signOut() {
      var auth2 = gapi.auth2.getAuthInstance();
      auth2.signOut().then(function () {
        console.log('User signed out.');
      });
    },*/
    saveMotivation(name, aim){
      this.$refs.motivationToAdd.addMotivation(name, aim)
    }
  }
  
}
</script>

<style>
* {
  margin: 0 !important;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #000000;
  
}
.button-add {
  background-image: url(./assets/add_icon.png);
  border-radius: 8px;
  background-size: 46px, 46px;
  background-repeat: no-repeat;
  height: 50px;
  width: 50px;
}
.header {
  background-color: rgb(71, 225, 245);
}
h1 {
  padding-top: 20px;
  padding-bottom: 20px;
  padding-left: 10px;
  text-align: left;
}
.google {
  padding: 10px;
  float: right;
}
</style>
