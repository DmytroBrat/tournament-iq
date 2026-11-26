import { CalendarDays, MapPin, Trophy, UsersRound, X } from 'lucide-react'
import { useEffect, useRef, useState, type FormEvent } from 'react'
import { ApiError, type MatchInput, type NamedResource } from '../api'
import { instantFromLocal, timeZone } from '../dates'

const settings = {
  tournament: { title: 'New tournament', action: 'Create tournament', icon: Trophy, label: 'Tournament name', example: 'Riverside Cup' },
  team: { title: 'Add team', action: 'Register team', icon: UsersRound, label: 'Team name', example: 'Riverside Rovers' },
  field: { title: 'Add field', action: 'Register field', icon: MapPin, label: 'Field name', example: 'Main Pitch' },
  match: { title: 'Add match', action: 'Add match', icon: CalendarDays },
} as const

export type EntryKind = keyof typeof settings

type EntryDialogProps = {
  teams: NamedResource[]
  fields: NamedResource[]
  onClose: () => void
} & (
  { kind: 'match'; onSubmit: (match: MatchInput) => Promise<void> }
  | { kind: Exclude<EntryKind, 'match'>; onSubmit: (name: string) => Promise<void> }
)

function matchFromForm(form: FormData): MatchInput {
  const homeTeamId = String(form.get('homeTeamId') ?? '')
  const awayTeamId = String(form.get('awayTeamId') ?? '')
  const fieldId = String(form.get('fieldId') ?? '')
  const startsAt = instantFromLocal(String(form.get('startsAt') ?? ''))
  const endsAt = instantFromLocal(String(form.get('endsAt') ?? ''))
  if (homeTeamId === awayTeamId) throw new ApiError('Choose two different teams.', { awayTeamId: 'Choose two different teams.' })
  const unavailable = 'Choose a time that exists in your time zone.'
  if (!startsAt) throw new ApiError(unavailable, { startsAt: unavailable })
  if (!endsAt) throw new ApiError(unavailable, { endsAt: unavailable })
  if (Date.parse(endsAt) <= Date.parse(startsAt)) {
    const message = 'End time must be after start time.'
    throw new ApiError(message, { endsAt: message })
  }
  return { homeTeamId, awayTeamId, fieldId, startsAt, endsAt }
}

export function EntryDialog(props: EntryDialogProps) {
  const { kind, fields, teams, onClose } = props
  const dialog = useRef<HTMLDialogElement>(null)
  const saving = useRef(false)
  const [pending, setPending] = useState(false)
  const [error, setError] = useState<ApiError>()
  const { title, action, icon: Icon } = settings[kind]

  useEffect(() => {
    const element = dialog.current
    element?.showModal()
    return () => element?.close()
  }, [])

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    if (saving.current) return
    saving.current = true
    setPending(true)
    setError(undefined)
    const form = new FormData(event.currentTarget)

    try {
      if (props.kind === 'match') {
        await props.onSubmit(matchFromForm(form))
      } else {
        const name = String(form.get('name') ?? '').trim()
        if (!name) throw new ApiError('Enter a name.', { name: 'Enter a name.' })
        await props.onSubmit(name)
      }
      onClose()
    } catch (problem) {
      setError(problem instanceof ApiError ? problem : new ApiError('Could not save. Try again.'))
    } finally {
      saving.current = false
      setPending(false)
    }
  }

  function fieldStatus(name: string) {
    return error?.fields[name] ? { 'aria-invalid': true, 'aria-describedby': 'form-error' } : {}
  }

  const nameSettings = kind !== 'match' ? settings[kind] : null
  const errorMessages = error ? [...new Set(Object.values(error.fields))] : []

  return (
    <dialog
      className="entry-dialog"
      ref={dialog}
      aria-labelledby="entry-title"
      onCancel={(event) => {
        event.preventDefault()
        if (!saving.current) onClose()
      }}
    >
      <div className="dialog-header">
        <span className="dialog-icon"><Icon size={21} aria-hidden="true" /></span>
        <h2 id="entry-title">{title}</h2>
        <button className="icon-button" type="button" aria-label="Close" onClick={onClose} disabled={pending}>
          <X size={20} aria-hidden="true" />
        </button>
      </div>
      <form onSubmit={submit}>
        <fieldset disabled={pending} className="entry-fields">
          {nameSettings ? (
            <div className="form-field">
              <label htmlFor="entry-name">{nameSettings.label}</label>
              <input id="entry-name" name="name" maxLength={120} placeholder={nameSettings.example}
                autoComplete="off" required {...fieldStatus('name')} />
            </div>
          ) : (
            <>
              <div className="form-grid">
                {(['homeTeamId', 'awayTeamId'] as const).map((name) => (
                  <div className="form-field" key={name}>
                    <label htmlFor={name}>{name === 'homeTeamId' ? 'Home team' : 'Away team'}</label>
                    <select id={name} name={name} defaultValue="" required {...fieldStatus(name)}>
                      <option value="" disabled>Choose a team</option>
                      {teams.map((team) => <option key={team.id} value={team.id}>{team.name}</option>)}
                    </select>
                  </div>
                ))}
              </div>
              <div className="form-field">
                <label htmlFor="entry-field">Field</label>
                <select id="entry-field" name="fieldId" defaultValue="" required {...fieldStatus('fieldId')}>
                  <option value="" disabled>Choose a field</option>
                  {fields.map((field) => <option key={field.id} value={field.id}>{field.name}</option>)}
                </select>
              </div>
              <div className="form-grid">
                <div className="form-field">
                  <label htmlFor="entry-start">Start time</label>
                  <input id="entry-start" type="datetime-local" name="startsAt" required {...fieldStatus('startsAt')} />
                </div>
                <div className="form-field">
                  <label htmlFor="entry-end">End time</label>
                  <input id="entry-end" type="datetime-local" name="endsAt" required {...fieldStatus('endsAt')} />
                </div>
              </div>
              <p className="muted text-xs">Enter times in {timeZone}.</p>
            </>
          )}
        </fieldset>
        {error && <div className="form-error" id="form-error" role="alert">{errorMessages.join(' ') || error.message}</div>}
        <div className="dialog-actions">
          <button type="button" className="button secondary" onClick={onClose} disabled={pending}>Cancel</button>
          <button type="submit" className="button primary" disabled={pending}>{pending ? 'Saving…' : action}</button>
        </div>
      </form>
    </dialog>
  )
}
