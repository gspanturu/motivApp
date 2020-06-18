<template>
  <div class="motivationItem" :id="'motivation-' + this.item.id">
      <div>
          {{item.name}}
      </div>
      
      <br>
      <div>
        {{item.count}} von {{item.aim}}
      </div>
      <br>
      <div>
          <button class="increase-button" @click="increase">+</button>
      </div>
      
  </div>
</template>

<script>
export default {
    name: "Motivation",
    props: [
        'item'
    ],
    methods: {
        increase(){

            if(this.item.count < this.item.aim){
                this.$emit('increase', this.item)
            }
            else if(this.item.count >= this.item.aim){
                document.getElementById('motivation-' + this.item.id).disabled = true;
            }
            
        }
    }
}
</script>

<style>
    .motivationItem {
        border: black 1px solid;
        border-radius: 16px;
        width: 200px;
        height: 200px;
        margin: 10px !important;
        text-align: center;
        position:relative;
        float: left;
    }
    .motivationItem div{
        padding-top:25px;
    }
    .increase-button {
        border-radius: 15px;
        width:40px;
        height: 40px;
    }
</style>