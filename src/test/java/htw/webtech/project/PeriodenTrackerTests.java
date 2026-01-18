package htw.webtech.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PeriodenTrackerTests {

    // 1. Test: Service (Upsert verhindert Duplikate)
    @Nested
    @DisplayName("Service (1 Test)")
    @ExtendWith(MockitoExtension.class)
    class ServiceTest {

        @Mock
        private PeriodEntryRepository repo;

        @InjectMocks
        private PeriodEntryService service;

        @Test
        void upsertByDate_updatesExistingInsteadOfCreatingDuplicate() {
            LocalDate d = LocalDate.of(2026, 1, 1);

            PeriodEntry existing = new PeriodEntry();
            ReflectionTestUtils.setField(existing, "id", 5L);
            existing.setDate(d);
            existing.setSymptom("alt");

            PeriodEntry incoming = new PeriodEntry();
            incoming.setDate(d);
            incoming.setSymptom("neu");

            when(repo.findFirstByDateOrderByIdDesc(d)).thenReturn(Optional.of(existing));
            when(repo.save(any(PeriodEntry.class))).thenAnswer(inv -> inv.getArgument(0));

            PeriodEntry result = service.upsertByDate(incoming);

            // Wichtig: gleiche ID => Update, kein neuer DB-Eintrag pro Tag
            assertEquals(5L, result.getId());
            assertEquals("neu", result.getSymptom());

            verify(repo).findFirstByDateOrderByIdDesc(d);
            verify(repo).save(existing);
        }
    }

    // Die anderen 5 Tests: Controller (CRUD)
    @Nested
    @DisplayName("Controller (5 Tests, CRUD)")
    @WebMvcTest(controllers = PeriodEntryController.class)
    class ControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private PeriodEntryService service;

        private PeriodEntry entry(long id, LocalDate date, String symptom) {
            PeriodEntry e = new PeriodEntry();
            ReflectionTestUtils.setField(e, "id", id);
            e.setDate(date);
            e.setSymptom(symptom);
            e.setNote("note");
            e.setPeriode(true);
            e.setBleeding(2);
            e.setPain(1);
            e.setMood(3);
            e.setMeds(List.of("Wärme"));
            return e;
        }

        // 1) READ
        @Test
        void getEntries_returns200AndList() throws Exception {
            when(service.getAllEntries()).thenReturn(List.of(
                    entry(1, LocalDate.of(2026,1,1), "s1"),
                    entry(2, LocalDate.of(2026,1,2), "s2")
            ));

            mockMvc.perform(get("/api/v1/entries"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)));

            verify(service).getAllEntries();
        }

        // 2) CREATE (Upsert)
        @Test
        void postEntries_callsUpsert_returns200() throws Exception {
            PeriodEntry saved = entry(10, LocalDate.of(2026,1,1), "test");
            when(service.upsertByDate(any(PeriodEntry.class))).thenReturn(saved);

            String body = """
              {"date":"01-01-2026","symptom":"test","note":"note","periode":true,"bleeding":2,"pain":1,"mood":3,"meds":["Wärme"]}
            """;

            mockMvc.perform(post("/api/v1/entries")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(10)))
                    .andExpect(jsonPath("$.date", is("01-01-2026")));

            verify(service).upsertByDate(any(PeriodEntry.class));
        }

        // 3) UPDATE
        @Test
        void putEntries_callsUpdate_returns200() throws Exception {
            PeriodEntry updated = entry(5, LocalDate.of(2026,1,2), "updated");
            when(service.updateById(eq(5L), any(PeriodEntry.class))).thenReturn(updated);

            String body = """
              {"date":"02-01-2026","symptom":"updated","note":"note","periode":true,"bleeding":2,"pain":1,"mood":3,"meds":["Wärme"]}
            """;

            mockMvc.perform(put("/api/v1/entries/5")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(5)))
                    .andExpect(jsonPath("$.symptom", is("updated")));

            verify(service).updateById(eq(5L), any(PeriodEntry.class));
        }

        // 4) DELETE by ID
        @Test
        void deleteById_returns204() throws Exception {
            doNothing().when(service).deleteById(7L);

            mockMvc.perform(delete("/api/v1/entries/7"))
                    .andExpect(status().isNoContent());

            verify(service).deleteById(7L);
        }

        // 5) DELETE by Date
        @Test
        void deleteByDate_valid_returns204() throws Exception {
            when(service.deleteByDate(LocalDate.of(2026,1,1))).thenReturn(true);

            mockMvc.perform(delete("/api/v1/entries/by-date/01-01-2026"))
                    .andExpect(status().isNoContent());

            verify(service).deleteByDate(LocalDate.of(2026,1,1));
        }
    }
}
