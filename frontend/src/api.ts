export type NamedResource = {
  id: string
  name: string
}

export type Match = {
  id: string
  homeTeam: NamedResource
  awayTeam: NamedResource
  field: NamedResource
  startsAt: string
  endsAt: string
}

export type MatchInput = {
  homeTeamId: string
  awayTeamId: string
  fieldId: string
  startsAt: string
  endsAt: string
}

export type TournamentWorkspace = {
  teams: NamedResource[]
  fields: NamedResource[]
  schedule: Match[]
}

type Problem = {
  detail?: string
  errors?: { field: string; message: string }[]
}

export class ApiError extends Error {
  constructor(message: string, readonly fields: Record<string, string> = {}) {
    super(message)
  }
}

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  let response: Response
  try {
    response = await fetch('/api/tournaments' + path, options)
  } catch (error) {
    if (options.signal?.aborted) throw error
    throw new ApiError('Could not connect to TournamentIQ. Try again.')
  }

  if (!response.ok) {
    const problem: Problem | null = await response.json().catch(() => null)
    const errors = Object.fromEntries(problem?.errors?.map(({ field, message }) => [field, message]) ?? [])
    throw new ApiError(problem?.detail ?? 'The request could not be completed. Try again.', errors)
  }
  return response.json()
}

function post<T>(path: string, data: unknown) {
  return request<T>(path, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
}

export const api = {
  tournaments: (signal: AbortSignal) => request<NamedResource[]>('', { signal }),
  createTournament: (name: string) => post<NamedResource>('', { name }),
  createTeam: (id: string, name: string) => post<NamedResource>(`/${id}/teams`, { name }),
  createField: (id: string, name: string) => post<NamedResource>(`/${id}/fields`, { name }),
  createMatch: (id: string, match: MatchInput) => post<Match>(`/${id}/matches`, match),
  async workspace(id: string, signal: AbortSignal): Promise<TournamentWorkspace> {
    const [teams, fields, schedule] = await Promise.all([
      request<NamedResource[]>(`/${id}/teams`, { signal }),
      request<NamedResource[]>(`/${id}/fields`, { signal }),
      request<Match[]>(`/${id}/schedule`, { signal }),
    ])
    return { teams, fields, schedule }
  },
}
