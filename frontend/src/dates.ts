const locale = 'en-GB'
const clock = new Intl.DateTimeFormat(locale, { hour: '2-digit', minute: '2-digit', hourCycle: 'h23' })
const day = new Intl.DateTimeFormat(locale, { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' })
const summary = new Intl.DateTimeFormat(locale, { day: 'numeric', month: 'short', year: 'numeric' })

export const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone

export function formatTime(value: string) {
  return clock.format(new Date(value))
}

export function formatDay(value: string) {
  return day.format(new Date(value))
}

export function formatDateRange(first: string, last: string) {
  return summary.formatRange(new Date(first), new Date(last))
}

export function localDateKey(value: string | Date) {
  const date = new Date(value)
  const year = String(date.getFullYear()).padStart(4, '0')
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

export function instantFromLocal(value: string) {
  const date = new Date(value)
  if (Number.isNaN(date.getTime()) || `${localDateKey(date)}T${clock.format(date)}` !== value) {
    return undefined
  }
  return date.toISOString()
}
