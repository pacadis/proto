package models;

import java.io.Serializable;
import java.util.Objects;

public class ParticipantCursaDTO implements Serializable,Entity<Integer> {
    private int id;
    private String numeParticipant;
    private int capCursa;

    public ParticipantCursaDTO(int id,String numeParticipant, int capCursa) {
        this.id=id;
        this.numeParticipant = numeParticipant;
        this.capCursa = capCursa;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer integer) {
        this.id=integer;
    }

    public String getNumeParticipant() {
        return numeParticipant;
    }

    public void setNumeParticipant(String numeParticipant) {
        this.numeParticipant = numeParticipant;
    }

    public int getCapCursa() {
        return capCursa;
    }

    public void setCapCursa(int capCursa) {
        this.capCursa = capCursa;
    }

    @Override
    public String toString() {
        return "ParticipantCursaDTO{" +
                "numeParticipant='" + numeParticipant + '\'' +
                ", capCursa=" + capCursa +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantCursaDTO that = (ParticipantCursaDTO) o;
        return capCursa == that.capCursa &&
                Objects.equals(numeParticipant, that.numeParticipant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeParticipant, capCursa);
    }


}
