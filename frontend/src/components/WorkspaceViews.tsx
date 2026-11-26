import { CalendarDays, ChevronDown, MapPin, Plus, UsersRound } from 'lucide-react'
import { useState } from 'react'
import type { Match, NamedResource } from '../api'
import { formatDay, formatTime, localDateKey, timeZone } from '../dates'

type ScheduleProps = {
  matches: Match[]
  fields: NamedResource[]
  canAddMatch: boolean
  onAdd: () => void
}

function TeamBadge({ team }: { team: NamedResource }) {
  const initials = team.name.split(/\s+/).slice(0, 2).map((word) => Array.from(word)[0]).join('')
  const color = Array.from(team.id).reduce((sum, character) => sum + character.charCodeAt(0), 0) % 6
  return <span className={`team-badge team-badge--${color}`} aria-hidden="true">{initials}</span>
}

function MatchTeam({ team }: { team: NamedResource }) {
  return <span className="match-team"><TeamBadge team={team} /><span>{team.name}</span></span>
}

function MatchEnd({ match }: { match: Match }) {
  const endsOnAnotherDay = localDateKey(match.startsAt) !== localDateKey(match.endsAt)
  return <time dateTime={match.endsAt} title={formatDay(match.endsAt)}>
    {formatTime(match.endsAt)}
    {endsOnAnotherDay && <span className="end-day">{formatDay(match.endsAt)}</span>}
  </time>
}

function MatchDay({ matches }: { matches: Match[] }) {
  const heading = formatDay(matches[0].startsAt)
  return (
    <section className="schedule-day" aria-label={heading}>
      <h3 className="day-heading"><CalendarDays size={16} aria-hidden="true" />{heading}</h3>
      <table className="schedule-table" aria-label={`Matches on ${heading}`}>
        <thead>
          <tr>
            <th scope="col">Matchup</th>
            <th scope="col">Field</th>
            <th scope="col">Start <span className="sr-only">time</span></th>
            <th scope="col">End <span className="sr-only">time</span></th>
          </tr>
        </thead>
        <tbody>
          {matches.map((match) => (
            <tr key={match.id}>
              <td>
                <div className="matchup">
                  <MatchTeam team={match.homeTeam} />
                  <span className="versus">vs</span>
                  <MatchTeam team={match.awayTeam} />
                </div>
              </td>
              <td><span className="field-name"><MapPin size={14} aria-hidden="true" />{match.field.name}</span></td>
              <td className="match-time font-semibold"><time dateTime={match.startsAt}>{formatTime(match.startsAt)}</time></td>
              <td className="match-time muted"><MatchEnd match={match} /></td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  )
}

export function Schedule({ matches, fields, canAddMatch, onAdd }: ScheduleProps) {
  const [field, setField] = useState('')
  const visible = field ? matches.filter((match) => match.field.id === field) : matches
  const days = new Map<string, Match[]>()
  for (const match of visible) {
    const date = localDateKey(match.startsAt)
    const matchesOnDay = days.get(date) ?? []
    matchesOnDay.push(match)
    days.set(date, matchesOnDay)
  }

  return (
    <>
      <div className="section-toolbar">
        <div>
          <h2>Match schedule</h2>
          <p>Times shown in {timeZone}</p>
        </div>
        <div className="toolbar-actions">
          {matches.length > 0 && (
            <label className="filter">
              <MapPin size={16} aria-hidden="true" />
              <span className="sr-only">Filter by field</span>
              <select value={field} onChange={(event) => setField(event.target.value)}>
                <option value="">All fields</option>
                {fields.map((item) => <option value={item.id} key={item.id}>{item.name}</option>)}
              </select>
              <ChevronDown size={14} aria-hidden="true" />
            </label>
          )}
          <button className="button primary" onClick={onAdd} disabled={!canAddMatch}>
            <Plus size={17} aria-hidden="true" /> Add match
          </button>
        </div>
      </div>
      {days.size > 0 ? (
        <div className="flex flex-col gap-4">
          {[...days].map(([date, matchesOnDay]) => <MatchDay key={date} matches={matchesOnDay} />)}
        </div>
      ) : (
        <div className="empty-state">
          <CalendarDays size={24} aria-hidden="true" />
          <h3>{field ? 'No matches on this field' : 'No matches scheduled'}</h3>
          <p>{field ? 'Choose a different field to see more matches.' : canAddMatch
            ? 'Add a match to start building the schedule.'
            : 'Register at least two teams and one field to add a match.'}</p>
          {field && <button className="button secondary" onClick={() => setField('')}>Clear filter</button>}
        </div>
      )}
    </>
  )
}

type DirectoryProps = {
  kind: 'team' | 'field'
  items: NamedResource[]
  onAdd: () => void
}

export function Directory({ kind, items, onAdd }: DirectoryProps) {
  const isTeam = kind === 'team'
  const Icon = isTeam ? UsersRound : MapPin
  return (
    <>
      <div className="section-toolbar">
        <div>
          <h2>{isTeam ? 'Registered teams' : 'Football fields'}</h2>
          <p>{items.length} {items.length === 1 ? kind : `${kind}s`} registered</p>
        </div>
        <button className="button primary" onClick={onAdd}><Plus size={17} aria-hidden="true" /> Add {kind}</button>
      </div>
      {items.length > 0 ? (
        <ul className="directory">
          {items.map((item) => (
            <li key={item.id}>
              {isTeam ? <TeamBadge team={item} /> : <span className="directory-icon"><Icon size={18} aria-hidden="true" /></span>}
              <span>{item.name}</span>
            </li>
          ))}
        </ul>
      ) : (
        <div className="empty-state">
          <Icon size={24} aria-hidden="true" />
          <h3>No {kind}s registered</h3>
          <p>Add a {kind} so you can assign it to a match.</p>
        </div>
      )}
    </>
  )
}
