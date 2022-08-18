<template>
  <table-index path="/opinion" :fields="fields"/>
</template>

<script>
/**
 * 常用意见页面
 */
export default {
  name: "index",
  data() {
    return {
      fields: {
        opinion: {label: '意见内容', required: true, search: true}
      }
    }
  }
}
</script>
