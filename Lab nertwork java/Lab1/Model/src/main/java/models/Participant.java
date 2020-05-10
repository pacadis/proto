package models;

import java.io.Serializable;
import java.util.Objects;

public class Participant implements Serializable,Entity<Integer> {
    private Integer id;
    private String nume;
    private int idEchipa;

    public Participant(Integer id, String nume, int idEchipa) {
        this.id = id;
        this.nume = nume;
        this.idEchipa = idEchipa;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getIdEchipa() {
        return idEchipa;
    }

    public void setIdEchipa(int idEchipa) {
        this.idEchipa = idEchipa;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", idEchipa=" + idEchipa +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return idEchipa == that.idEchipa &&
                Objects.equals(id, that.id) &&
                Objects.equals(nume, that.nume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nume, idEchipa);
    }
}
