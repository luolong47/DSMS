import colors from 'tailwindcss/colors'

/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./components/**/*.{js,vue,ts}",
    "./layouts/**/*.vue",
    "./pages/**/*.vue",
    "./plugins/**/*.{js,ts}",
    "./app.vue",
    "./error.vue",
    "./node_modules/@nuxt/ui-templates/**/*.{js,vue,ts}",
    "./node_modules/@nuxt/ui/**/*.{js,vue,ts}"
  ],
  theme: {
    extend: {
      colors: {
        ...colors,
        primary: colors.blue
      }
    },
  },
  plugins: [require('@tailwindcss/forms')],
}