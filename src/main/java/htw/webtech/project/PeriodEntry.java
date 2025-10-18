package htw.webtech.project;

public class PeriodEntry {

    private String date;
    private String symptom;
    private String note;

    public PeriodEntry() {}

    public PeriodEntry(String date, String symptom, String note) {
        this.date = date;
        this.symptom = symptom;
        this.note = note;
    }

    //Getter, Setter
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
}

