import { CalendarDays, ChevronRight, MapPin, Plus, Trophy, UsersRound, X } from 'lucide-react'
import { useEffect, useState } from 'react'
import { api, type MatchInput, type NamedResource, type TournamentWorkspace } from './api'
import { EntryDialog, type EntryKind } from './components/EntryDialog'
import { Directory, Schedule } from './components/WorkspaceViews'
import { formatDateRange } from './dates'

const sections = [
  { key: 'schedule', name: 'Schedule', countLabel: 'Scheduled matches', icon: CalendarDays },
  { key: 'teams', name: 'Teams', countLabel: 'Registered teams', icon: UsersRound },
  { key: 'fields', name: 'Fields', countLabel: 'Football fields', icon: MapPin },
] as const

type Section = typeof sections[number]['key']
type LoadResult<T> = { data: T; error: null } | { data: null; error: string }

function insertName(items: NamedResource[], item: NamedResource) {
  return [...items, item].sort((first, second) => first.name.localeCompare(second.name))
}

function errorMessage(error: unknown) {
  return error instanceof Error ? error.message : 'Something went wrong. Try again.'
}

function Retry({ message, onRetry }: { message: string; onRetry: () => void }) {
  return <div className="empty-state" role="alert">
    <h2>{message}</h2>
    <button className="button secondary" onClick={onRetry}>Try again</button>
  </div>
}

function App() {
  const [tournaments, setTournaments] = useState<LoadResult<NamedResource[]> | null>(null)
  const [selectedId, setSelectedId] = useState<string | null>(null)
  const [loaded, setLoaded] = useState<{ id: string; result: LoadResult<TournamentWorkspace> } | null>(null)
  const [listAttempt, setListAttempt] = useState(0)
  const [workspaceAttempt, setWorkspaceAttempt] = useState(0)
  const [section, setSection] = useState<Section>('schedule')
  const [entry, setEntry] = useState<EntryKind | null>(null)
  const [notice, setNotice] = useState('')

  const allTournaments = tournaments?.data ?? []
  const active = allTournaments.find((tournament) => tournament.id === selectedId)
  const workspace = loaded && loaded.id === selectedId ? loaded.result : null
  const data = workspace?.data
  const firstMatch = data?.schedule[0]
  const lastMatch = data?.schedule.at(-1)

  useEffect(() => {
    const controller = new AbortController()
    api.tournaments(controller.signal).then((items) => {
      if (controller.signal.aborted) return
      setTournaments({ data: items, error: null })
      setSelectedId((current) => current ?? items[0]?.id ?? null)
    }).catch((error: unknown) => {
      if (!controller.signal.aborted) setTournaments({ data: null, error: errorMessage(error) })
    })
    return () => controller.abort()
  }, [listAttempt])

  useEffect(() => {
    if (!selectedId) return
    const controller = new AbortController()
    api.workspace(selectedId, controller.signal).then((items) => {
      if (!controller.signal.aborted) setLoaded({ id: selectedId, result: { data: items, error: null } })
    }).catch((error: unknown) => {
      if (!controller.signal.aborted) setLoaded({ id: selectedId, result: { data: null, error: errorMessage(error) } })
    })
    return () => controller.abort()
  }, [selectedId, workspaceAttempt])

  useEffect(() => {
    if (!notice) return
    const timeout = window.setTimeout(() => setNotice(''), 5000)
    return () => window.clearTimeout(timeout)
  }, [notice])

  function selectTournament(id: string) {
    if (id === selectedId) return
    setSelectedId(id)
    setSection('schedule')
    setLoaded(null)
  }

  function updateWorkspace(id: string, update: (items: TournamentWorkspace) => TournamentWorkspace) {
    setLoaded((current) => {
      if (!current?.result.data || current.id !== id) return current
      return { id, result: { data: update(current.result.data), error: null } }
    })
  }

  async function submitName(name: string) {
    if (entry === 'tournament') {
      const created = await api.createTournament(name)
      setTournaments((current) => ({ data: insertName(current?.data ?? [], created), error: null }))
      selectTournament(created.id)
      setNotice('Tournament created.')
      return
    }

    if (!selectedId) return
    if (entry === 'team') {
      const team = await api.createTeam(selectedId, name)
      updateWorkspace(selectedId, (items) => ({ ...items, teams: insertName(items.teams, team) }))
      setNotice('Team registered.')
    } else if (entry === 'field') {
      const field = await api.createField(selectedId, name)
      updateWorkspace(selectedId, (items) => ({ ...items, fields: insertName(items.fields, field) }))
      setNotice('Field registered.')
    }
  }

  async function submitMatch(match: MatchInput) {
    if (!selectedId) return
    const created = await api.createMatch(selectedId, match)
    updateWorkspace(selectedId, (items) => ({
      ...items,
      schedule: [...items.schedule, created].sort(
        (first, second) => Date.parse(first.startsAt) - Date.parse(second.startsAt) || first.id.localeCompare(second.id),
      ),
    }))
    setNotice('Match added.')
  }

  const dialogProps = { teams: data?.teams ?? [], fields: data?.fields ?? [], onClose: () => setEntry(null) }

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <span className="brand-icon"><Trophy size={19} strokeWidth={2.4} aria-hidden="true" /></span>
          <span>Tournament<span className="brand-accent">IQ</span></span>
        </div>
        <nav className="tournament-nav" aria-label="Tournaments">
          <div className="nav-caption">Tournaments</div>
          {allTournaments.map((tournament) => (
            <button
              className="tournament-link"
              key={tournament.id}
              aria-current={selectedId === tournament.id ? 'page' : undefined}
              onClick={() => selectTournament(tournament.id)}
            >
              <Trophy size={16} aria-hidden="true" />
              <span>{tournament.name}</span>
              {selectedId === tournament.id && <ChevronRight size={15} className="nav-chevron" aria-hidden="true" />}
            </button>
          ))}
        </nav>
      </aside>

      <div className="app-content">
        <header className="topbar">
          <div className="breadcrumbs">
            <span>Tournaments</span>
            {active && <><ChevronRight size={15} aria-hidden="true" /><strong>{active.name}</strong></>}
          </div>
          <div className="mobile-switch">
            {allTournaments.length > 0 && <select
              aria-label="Tournament"
              value={selectedId ?? ''}
              onChange={(event) => selectTournament(event.target.value)}
            >
              {allTournaments.map((tournament) => <option key={tournament.id} value={tournament.id}>{tournament.name}</option>)}
            </select>}
          </div>
          <button className="button secondary new-tournament" disabled={!tournaments?.data} onClick={() => setEntry('tournament')}>
            <Plus size={17} aria-hidden="true" /> New tournament
          </button>
        </header>

        <main className="main-content">
          {tournaments?.error && <Retry message={tournaments.error} onRetry={() => {
            setTournaments(null)
            setListAttempt((attempt) => attempt + 1)
          }} />}
          {!tournaments && <p className="loading" role="status">Loading tournaments…</p>}
          {tournaments?.data?.length === 0 && (
            <div className="empty-state">
              <Trophy size={26} aria-hidden="true" />
              <h1>No tournaments yet</h1>
              <p>Create a tournament to register teams, add fields, and schedule matches.</p>
              <button className="button primary" onClick={() => setEntry('tournament')}>Create tournament</button>
            </div>
          )}

          {active && (
            <>
              <section className="tournament-hero">
                <div className="hero-content">
                  <h1>{active.name}</h1>
                  {firstMatch && lastMatch && (
                    <p><CalendarDays size={17} aria-hidden="true" />{formatDateRange(firstMatch.startsAt, lastMatch.startsAt)}</p>
                  )}
                </div>
              </section>

              {workspace?.error && <Retry message={workspace.error} onRetry={() => {
                setLoaded(null)
                setWorkspaceAttempt((attempt) => attempt + 1)
              }} />}
              {!workspace && <p className="loading" role="status">Loading tournament…</p>}

              {data && (
                <>
                  <div className="metrics">
                    {sections.map(({ key, countLabel, icon: Icon }) => (
                      <button className="metric" key={key} onClick={() => setSection(key)}>
                        <span className={`metric-icon metric-icon--${key}`}><Icon size={21} aria-hidden="true" /></span>
                        <span className="metric-content"><strong>{data[key].length}</strong><span>{countLabel}</span></span>
                        <ChevronRight className="metric-chevron" size={17} aria-hidden="true" />
                      </button>
                    ))}
                  </div>
                  <nav className="section-nav" aria-label="Tournament sections">
                    {sections.map(({ key, name, icon: Icon }) => (
                      <button key={key} onClick={() => setSection(key)} aria-current={section === key ? 'page' : undefined}>
                        <Icon size={17} aria-hidden="true" /> {name}
                      </button>
                    ))}
                  </nav>
                  {section === 'schedule' && <Schedule key={active.id} matches={data.schedule} fields={data.fields}
                    canAddMatch={data.teams.length >= 2 && data.fields.length > 0} onAdd={() => setEntry('match')} />}
                  {section === 'teams' && <Directory kind="team" items={data.teams} onAdd={() => setEntry('team')} />}
                  {section === 'fields' && <Directory kind="field" items={data.fields} onAdd={() => setEntry('field')} />}
                </>
              )}
            </>
          )}
        </main>
      </div>
      <div className="toast-region" role="status">
        {notice && <div className="toast">
          {notice}<button aria-label="Dismiss message" onClick={() => setNotice('')}><X size={17} aria-hidden="true" /></button>
        </div>}
      </div>
      {entry === 'match' && <EntryDialog key={entry} kind={entry} onSubmit={submitMatch} {...dialogProps} />}
      {entry && entry !== 'match' && <EntryDialog key={entry} kind={entry} onSubmit={submitName} {...dialogProps} />}
    </div>
  )
}

export default App
