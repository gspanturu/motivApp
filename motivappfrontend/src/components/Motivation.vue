<template>
  <div class="motivationItem">
      {{item.name}}
      <br>
      {{item.count}}
  </div>
</template>

<script>
export default {
    name: "Motivation",
    props: [
        'item'
    ]
}
</script>

<style>
    .motivationItem {
        border: black 1px solid;
        border-radius: 16px;
        width: 200px;
        height: 200px;
        margin: 10px;
        text-align: center;
    }
</style>