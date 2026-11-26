package com.artisyn.tournamentiq;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("demo")
class TournamentApiIntegrationTest {
    private static final String TOURNAMENTS = "/api/tournaments";
    private static final String DEMO = TOURNAMENTS + "/10000000-0000-4000-8000-000000000001";
    private static final String DEMO_HOME = "20000000-0000-4000-8000-000000000001";
    private static final String DEMO_AWAY = "20000000-0000-4000-8000-000000000002";
    private static final String DEMO_FIELD = "30000000-0000-4000-8000-000000000001";

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper json;

    @Test
    void createsTournamentAndRegistrationsThenListsMatchesByStartTime() throws Exception {
        ResultActions creation = submit(TOURNAMENTS, Map.of("name", "  Integration Cup  "))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Integration Cup"));
        String tournamentId = responseId(creation);
        String tournament = TOURNAMENTS + "/" + tournamentId;
        creation.andExpect(header().string("Location", tournament));

        mvc.perform(get(TOURNAMENTS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == '%s')].name", tournamentId).value("Integration Cup"));
        mvc.perform(get(tournament))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tournamentId));

        String home = register(tournament + "/teams", "Academy City");
        String away = register(tournament + "/teams", "Harbor United");
        String field = register(tournament + "/fields", "Old Park");

        mvc.perform(get(tournament + "/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Academy City"));
        mvc.perform(get(tournament + "/fields"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(field));

        String later = responseId(submit(tournament + "/matches",
                match(home, away, field, "2026-11-28T11:00:00+01:00", "2026-11-28T12:00:00+01:00"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.startsAt").value("2026-11-28T10:00:00Z")));
        String earlier = responseId(submit(tournament + "/matches",
                match(away, home, field, "2026-11-28T09:00:00Z", "2026-11-28T10:00:00Z"))
                .andExpect(status().isCreated()));

        mvc.perform(get(tournament + "/schedule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(earlier))
                .andExpect(jsonPath("$[1].id").value(later))
                .andExpect(jsonPath("$[1].homeTeam.name").value("Academy City"))
                .andExpect(jsonPath("$[1].field.name").value("Old Park"));
    }

    @Test
    void loadsFixedDemoScheduleThroughFlyway() throws Exception {
        mvc.perform(get(DEMO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Riverside Cup 2026"));
        mvc.perform(get(DEMO + "/schedule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(8))
                .andExpect(jsonPath("$[0].id").value("40000000-0000-4000-8000-000000000001"))
                .andExpect(jsonPath("$[0].startsAt").value("2026-11-28T09:00:00Z"));
    }

    @Test
    void rejectsTeamsAndFieldsRegisteredInADifferentTournament() throws Exception {
        String other = TOURNAMENTS + "/" + register(TOURNAMENTS, "Other Cup");
        String foreignTeam = register(other + "/teams", "Visiting Team");
        String foreignField = register(other + "/fields", "Visiting Pitch");
        String start = "2026-11-29T10:00:00Z";
        String end = "2026-11-29T11:00:00Z";

        expectFieldError(submit(DEMO + "/matches", match(DEMO_HOME, foreignTeam, DEMO_FIELD, start, end)), "awayTeamId");
        expectFieldError(submit(DEMO + "/matches", match(DEMO_HOME, DEMO_AWAY, foreignField, start, end)), "fieldId");
        mvc.perform(get(other + "/schedule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void returnsProblemsForInvalidInputAndMissingTournaments() throws Exception {
        expectFieldError(submit(TOURNAMENTS, Map.of("name", "  ")), "name");
        expectFieldError(submit(DEMO + "/matches",
                match(DEMO_HOME, DEMO_HOME, DEMO_FIELD, "2026-11-28T09:00:00Z", "2026-11-28T10:00:00Z")), "awayTeamId");
        expectFieldError(submit(DEMO + "/matches",
                match(DEMO_HOME, DEMO_AWAY, DEMO_FIELD, "2026-11-28T10:00:00Z", "2026-11-28T09:00:00Z")), "endsAt");

        mvc.perform(post(TOURNAMENTS).contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));

        mvc.perform(get(TOURNAMENTS + "/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.code").value("HTTP_400"));

        mvc.perform(get(TOURNAMENTS + "/00000000-0000-4000-8000-000000000000/schedule"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.code").value("TOURNAMENT_NOT_FOUND"));
    }

    private void expectFieldError(ResultActions response, String field) throws Exception {
        response.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.errors[0].field").value(field));
    }

    private String register(String path, String name) throws Exception {
        return responseId(submit(path, Map.of("name", name)).andExpect(status().isCreated()));
    }

    private ResultActions submit(String path, Map<String, String> body) throws Exception {
        return mvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsBytes(body)));
    }

    private String responseId(ResultActions response) throws Exception {
        JsonNode body = json.readTree(response.andReturn().getResponse().getContentAsByteArray());
        return body.get("id").asText();
    }

    private Map<String, String> match(String home, String away, String field, String start, String end) {
        return Map.of("homeTeamId", home, "awayTeamId", away, "fieldId", field, "startsAt", start, "endsAt", end);
    }
}
