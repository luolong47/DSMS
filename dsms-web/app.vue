<template>
  <NuxtLayout :name="$route.path === '/login' ? 'empty' : 'default'">
    <NuxtPage />
  </NuxtLayout>
</template>

<script setup>
import { useRoute } from 'vue-router'
const route = useRoute()
</script>
