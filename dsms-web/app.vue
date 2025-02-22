<template>
  <NuxtLayout :name="$route.path === '/login' ? 'empty' : 'default'">
    <NuxtPage />
  </NuxtLayout>
</template>

<script setup>
const route = useRoute()
</script>
