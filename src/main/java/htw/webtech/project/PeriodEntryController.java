package htw.webtech.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class PeriodEntryController {

    private final PeriodEntryService service;

    public PeriodEntryController(PeriodEntryService service) {
        this.service = service;
    }

    // READ
    @GetMapping("/entries")
    public ResponseEntity<List<PeriodEntry>> getEntries() {
        return ResponseEntity.ok(service.getAllEntries());
    }

    // CREATE (Upsert by date, verhindert Duplikate)
    @PostMapping("/entries")
    public ResponseEntity<PeriodEntry> upsert(@RequestBody PeriodEntry entry) {
        return ResponseEntity.ok(service.upsertByDate(entry));
    }

    // UPDATE
    @PutMapping("/entries/{id}")
    public ResponseEntity<PeriodEntry> update(@PathVariable Long id, @RequestBody PeriodEntry entry) {
        return ResponseEntity.ok(service.updateById(id, entry));
    }

    // DELETE by ID
    @DeleteMapping("/entries/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE by date (löscht auch alte Duplikate!)
    @DeleteMapping("/entries/by-date/{date}")
    public ResponseEntity<Void> deleteByDate(@PathVariable String date) {
        try {
            LocalDate parsed = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            boolean deleted = service.deleteByDate(parsed);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
