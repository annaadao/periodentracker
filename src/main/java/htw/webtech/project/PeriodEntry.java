package htw.webtech.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class PeriodEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private String symptom;

    @Column(length = 2000)
    private String note;

    private Boolean periode;      // true/false
    private Integer bleeding;     // 1..3
    private Integer pain;         // 0..3
    private Integer mood;         // 1..5

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "period_entry_meds", joinColumns = @JoinColumn(name = "period_entry_id"))
    @Column(name = "med")
    private List<String> meds = new ArrayList<>();

    public PeriodEntry() {}

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getPeriode() {
        return periode;
    }

    public void setPeriode(Boolean periode) {
        this.periode = periode;
    }

    public Integer getBleeding() {
        return bleeding;
    }

    public void setBleeding(Integer bleeding) {
        this.bleeding = bleeding;
    }

    public Integer getPain() {
        return pain;
    }

    public void setPain(Integer pain) {
        this.pain = pain;
    }

    public Integer getMood() {
        return mood;
    }

    public void setMood(Integer mood) {
        this.mood = mood;
    }

    public List<String> getMeds() {
        return meds;
    }

    public void setMeds(List<String> meds) {
        this.meds = (meds == null) ? new ArrayList<>() : meds;
    }
}

//Hallo