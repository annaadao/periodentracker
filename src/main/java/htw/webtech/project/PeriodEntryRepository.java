package htw.webtech.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PeriodEntryRepository extends JpaRepository<PeriodEntry, Long> {

    @Modifying
    @Query("delete from PeriodEntry p where p.date = :date")
    int deleteAllByDate(@Param("date") LocalDate date);
}


