<template>
<div class="motivation-list" ondrop="drop(event)" ondragover="allowDrop(event)">
    <Motivation 
    v-for="motivation in motivations" :key="motivation.id"
    :item="motivation"
    draggable="true" ondragstart="drag(event)"
    />
</div>
  
</template>

<script>
import Motivation from "./Motivation"
export default {
    name: "MotivationList",
    components: {
        Motivation
    },
    data: () =>({
        motivations: [
            {id: 1, name: "Schritte", aim: 10000},
            {id: 2, name: "Training", aim: 5}
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
        addMotivation(name, aim){
            var nextId = this.nextId()
            this.motivations.push({id: nextId, name: name, aim: aim})
            alert(this.motivations[nextId - 1].id)
        }
    }
}
</script>

<style>
    Motivation {
        padding: 10px;
    }
    .motivation-list {
        width: 500px;
        height: 500px;
    }
</style>