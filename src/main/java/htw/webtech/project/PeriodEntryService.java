package htw.webtech.project;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PeriodEntryService {

    private final PeriodEntryRepository repo;

    public PeriodEntryService(PeriodEntryRepository repo) {
        this.repo = repo;
    }

    public List<PeriodEntry> getAllEntries() {
        return repo.findAll();
    }

    public PeriodEntry create(PeriodEntry entry) {
        return repo.save(entry);
    }
}
