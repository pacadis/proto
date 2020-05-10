package models;

import java.io.Serializable;
import java.util.Objects;

public class Echipa implements Serializable,Entity<Integer> {
    private Integer id;
    private String nume;

    public Echipa(Integer id, String nume) {
        this.id = id;
        this.nume = nume;
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

    @Override
    public String toString() {
        return "Echipa{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Echipa echipa = (Echipa) o;
        return Objects.equals(id, echipa.id) &&
                Objects.equals(nume, echipa.nume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nume);
    }
}
