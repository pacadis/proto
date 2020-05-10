package models;

import java.io.Serializable;
import java.util.Objects;

public class CursaDTO implements Serializable,Entity<Integer>{
    private int id;
    private int capMotor;
    private int nrParticipanti;

    public CursaDTO(int id,int capMotor, int nrParticipanti) {
        this.id=id;
        this.capMotor = capMotor;
        this.nrParticipanti = nrParticipanti;
    }

    public int getCapMotor() {
        return capMotor;
    }

    public void setCapMotor(int capMotor) {
        this.capMotor = capMotor;
    }

    public int getNrParticipanti() {
        return nrParticipanti;
    }

    public void setNrParticipanti(int nrParticipanti) {
        this.nrParticipanti = nrParticipanti;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer integer) {
        this.id=integer;
    }

    @Override
    public String toString() {
        return "CursaDTO{" +
                "id=" + id +
                ", capMotor=" + capMotor +
                ", nrParticipanti=" + nrParticipanti +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CursaDTO cursaDTO = (CursaDTO) o;
        return id == cursaDTO.id &&
                capMotor == cursaDTO.capMotor &&
                nrParticipanti == cursaDTO.nrParticipanti;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, capMotor, nrParticipanti);
    }
}
