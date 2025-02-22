<template>
  <VCalendarDatePicker
    v-model="date"
    v-bind="{ ...attrs, ...$attrs }"
    @dayclick="onDayClick"
  />
</template>

<script setup lang="ts">
import { DatePicker as VCalendarDatePicker } from 'v-calendar'
import { computed } from 'vue'// @ts-ignore
import type { DatePickerDate, DatePickerRangeObject } from 'v-calendar/dist/types/src/use/datePicker'
import 'v-calendar/dist/style.css'

defineOptions({
  inheritAttrs: false
})

const props = defineProps({
  modelValue: {
    type: [Date, String] as PropType<DatePickerDate | null>,
    default: null
  },
  minDate: {
    type: [Date, String],
    default: undefined
  },
  maxDate: {
    type: [Date, String],
    default: undefined
  },
  placeholder: {
    type: String,
    default: '选择日期'
  },
  color: {
    type: String,
    default: 'primary'
  },
  variant: {
    type: String,
    default: 'soft'
  },
  icon: {
    type: String,
    default: null
  }
})

const emit = defineEmits(['update:model-value', 'close'])

const date = computed({
  get: () => props.modelValue ? new Date(props.modelValue) : null,
  set: (value) => {
    emit('update:model-value', value)
    emit('close')
  }
})

const attrs = {
  'transparent': true,
  'borderless': true,
  'color': 'primary',
  'is-dark': { selector: 'html', darkClass: 'dark' },
  'first-day-of-week': 2,
  'locale': 'zh-CN'
}

function onDayClick(_: unknown, event: MouseEvent): void {
  const target = event.target as HTMLElement
  target.blur()
}

const formatDate = (dateStr: string): string => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

const clearDate = (e: Event): void => {
  if (e) {
    e.preventDefault()
    e.stopPropagation()
  }
  emit('update:model-value', '')
}

const closePopover = (): void => {
  const popover = document.querySelector('.u-popover')
  if (popover) {
    const closeButton = popover.querySelector('.u-popover__close') as HTMLElement
    if (closeButton) {
      closeButton.click()
    }
  }
}
</script>

<style>
:root {
  --vc-gray-50: rgb(var(--color-gray-50));
  --vc-gray-100: rgb(var(--color-gray-100));
  --vc-gray-200: rgb(var(--color-gray-200));
  --vc-gray-300: rgb(var(--color-gray-300));
  --vc-gray-400: rgb(var(--color-gray-400));
  --vc-gray-500: rgb(var(--color-gray-500));
  --vc-gray-600: rgb(var(--color-gray-600));
  --vc-gray-700: rgb(var(--color-gray-700));
  --vc-gray-800: rgb(var(--color-gray-800));
  --vc-gray-900: rgb(var(--color-gray-900));
}

.vc-primary {
  --vc-accent-50: rgb(var(--color-primary-50));
  --vc-accent-100: rgb(var(--color-primary-100));
  --vc-accent-200: rgb(var(--color-primary-200));
  --vc-accent-300: rgb(var(--color-primary-300));
  --vc-accent-400: rgb(var(--color-primary-400));
  --vc-accent-500: rgb(var(--color-primary-500));
  --vc-accent-600: rgb(var(--color-primary-600));
  --vc-accent-700: rgb(var(--color-primary-700));
  --vc-accent-800: rgb(var(--color-primary-800));
  --vc-accent-900: rgb(var(--color-primary-900));
}

.vc-container {
  --vc-font-family: inherit;
  --vc-rounded-lg: 0.75rem;
  --vc-header-title-color: theme('colors.gray.900');
  --vc-header-arrow-color: theme('colors.gray.600');
  --vc-header-arrow-hover-color: theme('colors.gray.900');
  --vc-header-background: theme('colors.white');
  --vc-weekday-color: theme('colors.gray.500');
  --vc-weekday-background: theme('colors.gray.50');
  --vc-day-color: theme('colors.gray.700');
  --vc-day-border-color: theme('colors.gray.100');
  --vc-day-disabled-color: theme('colors.gray.300');
  --vc-day-hover-background: theme('colors.gray.50');
  --vc-day-current-color: theme('colors.indigo.600');
  --vc-day-selected-color: theme('colors.white');
  --vc-day-selected-background: theme('colors.indigo.600');
  --vc-day-selected-hover-background: theme('colors.indigo.700');
  border: 1px solid theme('colors.gray.200');
  border-radius: var(--vc-rounded-lg);
  box-shadow: theme('boxShadow.lg');
  width: 280px;
}

.vc-header {
  padding: 0.75rem 0.5rem;
  font-weight: 600;
}

.vc-weeks {
  padding: 0.5rem;
}

.vc-day {
  height: 2.5rem;
  transition-property: all;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 200ms;
}

.vc-day:not(.is-selected):hover {
  background-color: theme('colors.gray.50');
  border-radius: var(--vc-rounded-lg);
}

.vc-day.is-selected {
  background: linear-gradient(to right, theme('colors.indigo.600'), theme('colors.purple.600'));
  border-radius: var(--vc-rounded-lg);
  font-weight: 500;
}

.vc-day.is-today {
  font-weight: 600;
  color: theme('colors.indigo.600');
}
</style>

<style scoped>
.u-popover {
  z-index: 100;
}
</style> 