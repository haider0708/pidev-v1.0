package tn.esprit.applicationgui;

public class Rapport {
    private int id;
    private String note;
    private String type;
    private int rdvId; // Ajout de l'attribut rdvId

    public Rapport(int id, String note, String type, int rdvId) {
        this.id = id;
        this.note = note;
        this.type = type;
        this.rdvId = rdvId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRdvId() {
        return rdvId;
    }

    public void setRdvId(int rdvId) {
        this.rdvId = rdvId;
    }
}
