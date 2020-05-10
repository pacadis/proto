package models;

import java.io.Serializable;
import java.util.Objects;

public class Inscriere implements Serializable,Entity<Integer> {
    private Integer id;
    private int idParticipant;
    private int idCursa;

    public Inscriere(Integer id, int idParticipant, int idCursa) {
        this.id = id;
        this.idParticipant = idParticipant;
        this.idCursa = idCursa;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(int idParticipant) {
        this.idParticipant = idParticipant;
    }

    public int getIdCursa() {
        return idCursa;
    }

    public void setIdCursa(int idCursa) {
        this.idCursa = idCursa;
    }

    @Override
    public String toString() {
        return "Inscriere{" +
                "id=" + id +
                ", idParticipant=" + idParticipant +
                ", idCursa=" + idCursa +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inscriere inscriere = (Inscriere) o;
        return idParticipant == inscriere.idParticipant &&
                idCursa == inscriere.idCursa &&
                Objects.equals(id, inscriere.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idParticipant, idCursa);
    }
}
