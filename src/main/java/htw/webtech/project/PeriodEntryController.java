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

    private final PeriodEntryService periodEntryService;
    private LocalDate date;

    public PeriodEntryController(PeriodEntryService periodEntryService) {
        this.periodEntryService = periodEntryService;
    }

    @GetMapping("/entries") // READ - Client fordert Daten an
    public ResponseEntity<List<PeriodEntry>> getEntries() {
        return ResponseEntity.ok(periodEntryService.getAllEntries());
    }

    @PostMapping("/entries") // CREATE - Client sendet Daten, Backend verarbeitet und speichert
    public PeriodEntry create(@RequestBody PeriodEntry entry) {
        return periodEntryService.create(entry);
    }

    @DeleteMapping("/entries/by-date/{date}")
    public ResponseEntity<Void> deleteByDate(@PathVariable String date) {
        try {
            LocalDate parsed = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            boolean deleted = periodEntryService.deleteByDate(parsed);
            return deleted ? ResponseEntity.noContent().build()
                    : ResponseEntity.notFound().build();
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().build();
        }
    }




}
