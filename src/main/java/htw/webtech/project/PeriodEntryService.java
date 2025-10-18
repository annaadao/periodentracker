package htw.webtech.project;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PeriodEntryService {

    public List<PeriodEntry> getAllEntries() {
        return List.of(
                new PeriodEntry("2025-10-17", "Krämpfe", "War müde"),
                new PeriodEntry("2025-10-18", "Kopfschmerzen", "Ziemlich stressig heute")
        );
    }
}
