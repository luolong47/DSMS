<template>
  <div>
    <UNotifications />
    <NuxtLayout :name="$route.path === '/login' ? 'empty' : 'default'">
      <NuxtPage />
    </NuxtLayout>
  </div>
</template>

<script setup>
const route = useRoute()
</script>
