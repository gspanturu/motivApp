import Vue from 'vue'
import App from './App.vue'
import VModal from 'vue-js-modal'
import VueGAPI from 'vue-gapi'


const apiConfig = {
  apiKey: 'AIzaSyApO_TpNkGkh72JLIjsSWLCFrvSxMVEVDM',
  clientId: '762212303946-2bf82g29aa5hkmg11gsqrbh831ujh722.apps.googleusercontent.com',
  discoveryDocs: ['https://sheets.googleapis.com/$discovery/rest?version=v4'],
  // see all available scopes here: https://developers.google.com/identity/protocols/googlescopes'
  scope: 'https://www.googleapis.com/auth/spreadsheets',
 
  // works only with `ux_mode: "prompt"`
  refreshToken: true,
}
 
// Use the plugin and pass along the configuration
Vue.use(VueGAPI, apiConfig)

Vue.config.productionTip = false
Vue.use(VModal)

new Vue({
  render: h => h(App),
}).$mount('#app')
