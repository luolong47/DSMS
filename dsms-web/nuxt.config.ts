// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  devtools: { enabled: process.env.NODE_ENV === 'development' },

  modules: [
    '@nuxtjs/tailwindcss',
    '@pinia/nuxt',
    '@nuxt/icon',
    '@nuxt/ui'
  ],

  tailwindcss: {
    exposeConfig: true
  },

  ui: {
    global: true
  },

  colorMode: {
    preference: 'light'
  },

  runtimeConfig: {
    public: {
      apiBase: process.env.NUXT_PUBLIC_API_BASE || 'http://localhost:8080'
    }
  },

  nitro: {
    routeRules: {
      '/api/**': {
        proxy: process.env.NUXT_PUBLIC_API_BASE || 'http://localhost:8080/api/**'
      }
    }
  },

  compatibilityDate: '2024-02-21'
})