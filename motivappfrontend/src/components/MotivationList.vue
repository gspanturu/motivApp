<template>
<div class="motivation-list" ondrop="drop(event)" ondragover="allowDrop(event)">
    <div class="motivation-item">
        <Motivation 
    v-for="motivation in motivations" :key="motivation.id"
    :item="motivation"
    draggable="true" ondragstart="drag(event)"
    @increase="increaseCount"
    />
    </div>
    
</div>
  
</template>

<script>
import Motivation from "./Motivation"
import axios from 'axios'
export default {
    name: "MotivationList",
    components: {
        Motivation
    },
    data: () =>({
        motivations: [
            {id: 1, name: "Schritte", aim: 10000, count: 6740},
            {id: 2, name: "Training", aim: 5, count: 0}
        ]
    }),
    computed: {
        
    },
    methods: {
        allowDrop(ev){
            ev.preventDefault();
        },
        drag(ev){
            ev.dataTransfer.setData("text", ev.target.id);
        },
        drop(ev){
            ev.preventDefault();
            var data = ev.dataTransfer.getData("text");
            ev.target.appendChild(document.getElementById(data));
        },
        nextId () {
            return this.motivations[this.motivations.length - 1].id + 1
        },
        async addMotivation(name, aim){
            
            var nextId = this.nextId()
            var bodyIntention = '{ "id": ' + nextId + ', "description": "' + name + '", "googleId": "' + null + '" }'
            try{
                await axios.post('http://localhost:8080/intentions/add', { body: bodyIntention})
            } catch (e) {
                alert('insert failed: \n' + e)
            }
            
            var bodyTask = ''
            for(var i = 0; i < aim; i++){
                bodyTask = '{ "id": ' + i + ', "intentionId": ' + nextId + ', "date": "' + Date.now() + '", "done": ' + false + ' }'
                try{
                    await axios.post('http://localhost:8080/tasks/add', {body: bodyTask})
                } catch (e) {
                    //alert('insert failed: \n' + e)
                }
            }
            try{
                this.motivations = JSON.parse(await axios.get('http://localhost:8080/intentions'))
            } catch (e) {
                alert('get failed: \n' + e)
            }
            //this.motivations.push({id: nextId, name: name, aim: aim, count: 0})
        },
        async increaseCount(item){
            item.count++;
            try{
                await axios.put('http://localhost:8080/tas')
            } catch (e) {
                alert('put failed: \n' + e)
            }
        }

    },
    created: async function(){
        try{
            this.motivations = JSON.parse(await axios.get('http://localhost:8080/intentions'))
        } catch (e){
            alert('get failed: \n' + e)
        }
    }
}
</script>

<style>
    .motivation-list {
        width: 100vw;
        height: 100vw;
    }
</style>