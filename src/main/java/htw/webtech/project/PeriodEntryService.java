package htw.webtech.project;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PeriodEntryService {

    private final PeriodEntryRepository repo;
    private LocalDate date;

    public PeriodEntryService(PeriodEntryRepository repo) {
        this.repo = repo;
    }

    public List<PeriodEntry> getAllEntries() {
        return repo.findAll();
    }

    public PeriodEntry create(PeriodEntry entry) {
        return repo.save(entry);
    }

    @Transactional
    public boolean deleteByDate(LocalDate date) {
        return repo.deleteAllByDate(date) > 0;
    }
}
