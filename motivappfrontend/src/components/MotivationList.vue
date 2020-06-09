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
        addMotivation(name, aim){
            var nextId = this.nextId()
            this.motivations.push({id: nextId, name: name, aim: aim, count: 0})
        },
        increaseCount(item){
            item.count++;
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