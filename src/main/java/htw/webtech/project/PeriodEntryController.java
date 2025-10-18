package htw.webtech.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PeriodEntryController {

    private final PeriodEntryService periodEntryService;

    public PeriodEntryController(PeriodEntryService periodEntryService) {
        this.periodEntryService = periodEntryService;
    }

    @GetMapping("/entries")
    public ResponseEntity<List<PeriodEntry>> getEntries() {
        return ResponseEntity.ok(periodEntryService.getAllEntries());
    }
}
