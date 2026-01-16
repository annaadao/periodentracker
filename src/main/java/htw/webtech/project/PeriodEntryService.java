package htw.webtech.project;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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

    // POST (verhindert Mehrfach-Speichern pro Tag)
    @Transactional
    public PeriodEntry upsertByDate(PeriodEntry incoming) {
        if (incoming.getDate() == null) {
            throw new IllegalArgumentException("date is required");
        }

        var existingOpt = repo.findFirstByDateOrderByIdDesc(incoming.getDate());

        if (existingOpt.isPresent()) {
            PeriodEntry e = existingOpt.get();
            apply(e, incoming);
            return repo.save(e);
        }

        // neu
        sanitize(incoming);
        return repo.save(incoming);
    }

    // PUT /entries/{id}
    @Transactional
    public PeriodEntry updateById(Long id, PeriodEntry incoming) {
        PeriodEntry e = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found: " + id));

        // date optional updaten:
        if (incoming.getDate() != null) e.setDate(incoming.getDate());

        apply(e, incoming);
        return repo.save(e);
    }

    private void apply(PeriodEntry target, PeriodEntry src) {
        target.setSymptom(src.getSymptom());
        target.setNote(src.getNote());
        target.setPeriode(src.getPeriode());
        target.setBleeding(src.getBleeding());
        target.setPain(src.getPain());
        target.setMood(src.getMood());

        if (src.getMeds() == null) target.setMeds(new ArrayList<>());
        else target.setMeds(new ArrayList<>(src.getMeds()));
    }

    private void sanitize(PeriodEntry e) {
        if (e.getMeds() == null) e.setMeds(new ArrayList<>());
    }

    // DELETE /entries/{id}
    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    // DELETE /entries/by-date/{date}
    @Transactional
    public boolean deleteByDate(LocalDate date) {
        return repo.deleteAllByDate(date) > 0;
    }
}
